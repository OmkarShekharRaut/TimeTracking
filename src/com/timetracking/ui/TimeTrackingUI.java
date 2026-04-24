package com.timetracking.ui;

import com.timetracking.domain.model.Attendance;
import com.timetracking.domain.model.BreakRecord;
import com.timetracking.domain.model.Report;
import com.timetracking.facade.TimeTrackingFacade;

import javax.swing.*;
import java.awt.*;

public class TimeTrackingUI extends JFrame {
    private final TimeTrackingFacade facade;

    private final JTextField attendanceIdField = new JTextField("1", 10);
    private final JTextField breakIdField = new JTextField("101", 10);
    private final JTextArea output = new JTextArea(14, 55);

    private Attendance currentAttendance;
    private BreakRecord activeBreak;

    public TimeTrackingUI(TimeTrackingFacade facade) {
        super("Time Tracking");
        this.facade = facade;

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JButton punchInBtn = new JButton("Punch In");
        JButton punchOutBtn = new JButton("Punch Out");
        JButton startBreakBtn = new JButton("Start Break");
        JButton endBreakBtn = new JButton("End Break");
        JButton hoursBtn = new JButton("Show Hours");
        JButton reportBtn = new JButton("Generate Report");

        punchInBtn.addActionListener(e -> {
            int id = readInt(attendanceIdField, "Attendance ID");
            if (id < 0) return;

            currentAttendance = facade.punchIn(id);
            if (currentAttendance != null) {
                append("Punch in OK for id=" + id);
            } else {
                append("Punch in FAILED for id=" + id + " (see console / exception-report.txt)");
            }
        });

        punchOutBtn.addActionListener(e -> {
            int id = readInt(attendanceIdField, "Attendance ID");
            if (id < 0) return;

            Attendance updated = facade.punchOut(id);
            if (updated != null && updated.getPunchOutTime() != null) {
                append("Punch out OK for id=" + id + " | totalHours=" + updated.getTotalWorkHours());
            } else {
                append("Punch out attempted for id=" + id + " (see console / exception-report.txt)");
            }
        });

        startBreakBtn.addActionListener(e -> {
            int bid = readInt(breakIdField, "Break ID");
            if (bid < 0) return;

            activeBreak = facade.startBreak(bid);
            if (activeBreak != null) {
                append("Break started breakId=" + bid);
            } else {
                append("Break start FAILED breakId=" + bid + " (see console / exception-report.txt)");
            }
        });

        endBreakBtn.addActionListener(e -> {
            facade.endBreak(activeBreak);
            if (activeBreak != null && activeBreak.isEnded()) {
                append("Break ended breakId=" + activeBreak.getBreakId() + " | breakHours=" + activeBreak.getBreakDuration());
                if (currentAttendance != null) {
                    try {
                        currentAttendance.addBreak(activeBreak);
                        append("Break added to attendance id=" + currentAttendance.getAttendanceId());
                    } catch (RuntimeException ex) {
                        append("Could not add break to attendance: " + ex.getMessage());
                    }
                } else {
                    append("Note: no current attendance in memory; break not attached.");
                }
            } else {
                append("End break attempted (see console / exception-report.txt if it failed)");
            }
            activeBreak = null;
        });

        hoursBtn.addActionListener(e -> {
            int id = readInt(attendanceIdField, "Attendance ID");
            if (id < 0) return;

            Attendance a = facade.findAttendanceById(id);
            if (a == null) {
                append("No attendance found for id=" + id);
                return;
            }
            append("Attendance id=" + id
                    + " | in=" + a.getPunchInTime()
                    + " | out=" + a.getPunchOutTime()
                    + " | breaks=" + a.getBreaks().size()
                    + " | totalHours=" + a.getTotalWorkHours());
        });

        reportBtn.addActionListener(e -> {
            Report report = facade.generateReport();
            append("Report: start=" + report.getStartDate()
                    + " end=" + report.getEndDate()
                    + " totalHours=" + report.getTotalHours());
        });

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

        c.gridx = 0;
        form.add(new JLabel("Attendance ID"), c);
        c.gridx = 1;
        form.add(attendanceIdField, c);
        c.gridx = 2;
        form.add(new JLabel("Break ID"), c);
        c.gridx = 3;
        form.add(breakIdField, c);

        JPanel buttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons1.add(punchInBtn);
        buttons1.add(punchOutBtn);
        buttons1.add(hoursBtn);

        JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons2.add(startBreakBtn);
        buttons2.add(endBreakBtn);
        buttons2.add(reportBtn);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(buttons1);
        controls.add(Box.createVerticalStrut(6));
        controls.add(buttons2);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.NORTH);
        add(controls, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private int readInt(JTextField field, String label) {
        String raw = field.getText() == null ? "" : field.getText().trim();
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            append(label + " must be an integer. Got: '" + raw + "'");
            return -1;
        }
    }

    private void append(String msg) {
        output.append(msg + "\n");
        output.setCaretPosition(output.getDocument().getLength());
    }
}

