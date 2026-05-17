import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentGradeManagerGUI extends JFrame {

    // ── Data ──────────────────────────────────────────────────────────────
    static final String[] SUBJECTS = {"Math", "Science", "English", "Computer"};
    static final int MAX  = 100;

    static class Student {
        String name;
        int[]  marks = new int[4];
        double pct;
        Student(String name, int[] marks) {
            this.name  = name;
            this.marks = marks;
            int t = 0; for (int m : marks) t += m;
            this.pct = t * 100.0 / (MAX * 4);
        }
    }

    ArrayList<Student> students = new ArrayList<>();

    // ── Palette ───────────────────────────────────────────────────────────
    static final Color BG        = new Color(15,  23,  42);   // navy
    static final Color CARD      = new Color(30,  41,  59);   // card bg
    static final Color ACCENT    = new Color(99, 102, 241);   // indigo
    static final Color ACCENT2   = new Color(139, 92, 246);   // violet
    static final Color GREEN     = new Color(34, 197,  94);
    static final Color RED       = new Color(239, 68,  68);
    static final Color YELLOW    = new Color(251,191, 36);
    static final Color TEXT      = new Color(226,232,240);
    static final Color SUBTEXT   = new Color(148,163,184);
    static final Color BORDER    = new Color(51, 65,  85);

    // ── Shared widgets ────────────────────────────────────────────────────
    JTextField  tfName;
    JTextField[]tfMarks = new JTextField[4];
    DefaultTableModel tableModel;
    JTable      table;
    JLabel[]    lbHigh = new JLabel[4], lbLow = new JLabel[4], lbAvg = new JLabel[4];
    JLabel      lbClassAvg, lbTopper, lbNeedsHelp;

    // ─────────────────────────────────────────────────────────────────────
    public StudentGradeManagerGUI() {
        setTitle("Student Grade Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0,0));
        getContentPane().setBackground(BG);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── HEADER ────────────────────────────────────────────────────────────
    JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER));

        JLabel title = new JLabel("  🎓  Student Grade Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(14,10,14,0));
        p.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("CodeAlpha Internship Project  ");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(SUBTEXT);
        p.add(sub, BorderLayout.EAST);
        return p;
    }

    // ── MAIN CENTER (left input + right table/stats) ───────────────────
    JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(12,0));
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(14,14,6,14));
        p.add(buildInputCard(), BorderLayout.WEST);
        p.add(buildRightPanel(), BorderLayout.CENTER);
        return p;
    }

    // ── INPUT CARD ────────────────────────────────────────────────────────
    JPanel buildInputCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(18,18,18,18)));
        card.setPreferredSize(new Dimension(260, 0));

        card.add(sectionLabel("Add Student"));
        card.add(Box.createVerticalStrut(14));

        card.add(fieldLabel("Student Name"));
        card.add(Box.createVerticalStrut(4));
        tfName = styledField();
        card.add(tfName);
        card.add(Box.createVerticalStrut(14));

        card.add(fieldLabel("Subject Marks  (0 – 100)"));
        card.add(Box.createVerticalStrut(8));

        Color[] dotColors = {ACCENT, GREEN, YELLOW, RED};
        for (int i = 0; i < 4; i++) {
            JPanel row = new JPanel(new BorderLayout(6,0));
            row.setBackground(CARD);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

            JLabel dot = new JLabel("●  " + SUBJECTS[i]);
            dot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            dot.setForeground(dotColors[i]);
            row.add(dot, BorderLayout.WEST);

            tfMarks[i] = styledField();
            tfMarks[i].setPreferredSize(new Dimension(70,30));
            tfMarks[i].setMaximumSize(new Dimension(70,30));
            row.add(tfMarks[i], BorderLayout.EAST);

            card.add(row);
            card.add(Box.createVerticalStrut(7));
        }

        card.add(Box.createVerticalStrut(18));

        JButton btnAdd = gradientButton("➕  Add Student", ACCENT, ACCENT2);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnAdd.addActionListener(e -> addStudent());
        card.add(btnAdd);

        card.add(Box.createVerticalStrut(10));

        JButton btnClear = outlineButton("🗑  Clear All");
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnClear.addActionListener(e -> clearAll());
        card.add(btnClear);

        return card;
    }

    // ── RIGHT PANEL (table + stats) ───────────────────────────────────────
    JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(0,12));
        p.setBackground(BG);
        p.add(buildTableCard(), BorderLayout.CENTER);
        p.add(buildStatsPanel(), BorderLayout.SOUTH);
        return p;
    }

    // ── TABLE ─────────────────────────────────────────────────────────────
    JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER,1,true),
            BorderFactory.createEmptyBorder(12,12,12,12)));

        JLabel lbl = sectionLabel("Student Records");
        lbl.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        card.add(lbl, BorderLayout.NORTH);

        String[] cols = {"#","Name","Math","Science","English","Computer","Percentage","Grade"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(CARD);
        sp.getViewport().setBackground(new Color(22,32,48));
        sp.setBorder(BorderFactory.createLineBorder(BORDER,1));
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    // ── STATS ROW ─────────────────────────────────────────────────────────
    JPanel buildStatsPanel() {
        JPanel row = new JPanel(new GridLayout(1, 7, 10, 0));
        row.setBackground(BG);

        Color[] dotColors = {ACCENT, GREEN, YELLOW, RED};

        for (int i = 0; i < 4; i++) {
            JPanel c = miniStatCard(SUBJECTS[i], dotColors[i]);
            lbHigh[i] = statVal("—", GREEN);
            lbLow[i]  = statVal("—", RED);
            lbAvg[i]  = statVal("—", YELLOW);
            c.add(tinyRow("▲ High", lbHigh[i]));
            c.add(tinyRow("▼ Low",  lbLow[i]));
            c.add(tinyRow("~ Avg",  lbAvg[i]));
            row.add(c);
        }

        // Class summary cards
        JPanel cAvg  = miniStatCard("Class Avg %", ACCENT);
        lbClassAvg   = statVal("—", ACCENT);
        cAvg.add(lbClassAvg);
        row.add(cAvg);

        JPanel cTop  = miniStatCard("Topper 🏆", GREEN);
        lbTopper     = statVal("—", GREEN);
        cTop.add(lbTopper);
        row.add(cTop);

        JPanel cNeed = miniStatCard("Needs Help ⚠", RED);
        lbNeedsHelp  = statVal("—", RED);
        cNeed.add(lbNeedsHelp);
        row.add(cNeed);

        return row;
    }

    // ── LOGIC: Add Student ────────────────────────────────────────────────
    void addStudent() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) { alert("Please enter the student's name."); return; }

        int[] marks = new int[4];
        for (int i = 0; i < 4; i++) {
            String txt = tfMarks[i].getText().trim();
            try {
                int m = Integer.parseInt(txt);
                if (m < 0 || m > 100) { alert(SUBJECTS[i] + " marks must be between 0 and 100."); return; }
                marks[i] = m;
            } catch (NumberFormatException ex) {
                alert("Enter a valid number for " + SUBJECTS[i] + "."); return;
            }
        }

        Student s = new Student(name, marks);
        students.add(s);

        // add table row
        int idx = students.size();
        tableModel.addRow(new Object[]{
            idx, s.name,
            s.marks[0], s.marks[1], s.marks[2], s.marks[3],
            String.format("%.1f%%", s.pct),
            grade(s.pct)
        });

        refreshStats();
        clearInputFields();
        tfName.requestFocus();
    }

    // ── LOGIC: Refresh Stats ──────────────────────────────────────────────
    void refreshStats() {
        int n = students.size();
        if (n == 0) return;

        for (int sub = 0; sub < 4; sub++) {
            int hi = Integer.MIN_VALUE, lo = Integer.MAX_VALUE;
            double sum = 0;
            for (Student s : students) {
                int m = s.marks[sub];
                sum += m;
                if (m > hi) hi = m;
                if (m < lo) lo = m;
            }
            lbHigh[sub].setText(hi + "");
            lbLow[sub].setText(lo  + "");
            lbAvg[sub].setText(String.format("%.1f", sum / n));
        }

        double classAvg = 0;
        Student topper = students.get(0), needsHelp = students.get(0);
        for (Student s : students) {
            classAvg += s.pct;
            if (s.pct > topper.pct)    topper    = s;
            if (s.pct < needsHelp.pct) needsHelp = s;
        }
        classAvg /= n;
        lbClassAvg.setText(String.format("%.1f%%", classAvg));
        lbTopper.setText("<html><center>" + topper.name + "<br><small>" +
                String.format("%.1f%%", topper.pct) + "</small></center></html>");
        lbNeedsHelp.setText("<html><center>" + needsHelp.name + "<br><small>" +
                String.format("%.1f%%", needsHelp.pct) + "</small></center></html>");
    }

    // ── LOGIC: Clear All ──────────────────────────────────────────────────
    void clearAll() {
        if (students.isEmpty()) return;
        int r = JOptionPane.showConfirmDialog(this,
            "Clear all student records?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        students.clear();
        tableModel.setRowCount(0);
        for (int i = 0; i < 4; i++) {
            lbHigh[i].setText("—"); lbLow[i].setText("—"); lbAvg[i].setText("—");
        }
        lbClassAvg.setText("—"); lbTopper.setText("—"); lbNeedsHelp.setText("—");
    }

    void clearInputFields() {
        tfName.setText("");
        for (JTextField tf : tfMarks) tf.setText("");
    }

    // ── STATUS BAR ────────────────────────────────────────────────────────
    JPanel buildStatus() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,14,4));
        p.setBackground(new Color(10,15,30));
        p.setBorder(BorderFactory.createMatteBorder(1,0,0,0,BORDER));
        JLabel l = new JLabel("CodeAlpha  ·  Java Programming Internship  ·  Harsh Raj");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(SUBTEXT);
        p.add(l);
        return p;
    }

    // ── UI Helpers ────────────────────────────────────────────────────────
    JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(TEXT);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(SUBTEXT);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(22,32,48));
        tf.setForeground(TEXT);
        tf.setCaretColor(TEXT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER,1,true),
            BorderFactory.createEmptyBorder(6,8,6,8)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        return tf;
    }

    JButton gradientButton(String text, Color c1, Color c2) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,c1,getWidth(),0,c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    JButton outlineButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(SUBTEXT);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBackground(CARD);
        b.setBorder(BorderFactory.createLineBorder(BORDER,1,true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    void styleTable(JTable t) {
        t.setBackground(new Color(22,32,48));
        t.setForeground(TEXT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(32);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0,4));
        t.setSelectionBackground(new Color(51,65,85));
        t.setSelectionForeground(TEXT);
        t.getTableHeader().setBackground(CARD);
        t.getTableHeader().setForeground(SUBTEXT);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER));

        // center-align all columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setBackground(new Color(22,32,48));
        center.setForeground(TEXT);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // grade column colored renderer
        t.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl,val,sel,foc,row,col);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBackground(new Color(22,32,48));
                String g = val != null ? val.toString() : "";
                l.setForeground(switch(g) {
                    case "A+" -> GREEN;
                    case "A"  -> new Color(74,222,128);
                    case "B"  -> YELLOW;
                    case "C"  -> new Color(251,146,60);
                    default   -> RED;
                });
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                return l;
            }
        });

        // column widths
        int[] widths = {30, 140, 70, 70, 70, 70, 90, 60};
        for (int i = 0; i < widths.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    JPanel miniStatCard(String title, Color accent) {
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBackground(CARD);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER,1,true),
            BorderFactory.createEmptyBorder(10,10,10,10)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(accent);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        c.add(lbl);
        c.add(Box.createVerticalStrut(6));
        return c;
    }

    JLabel statVal(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(color);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    JPanel tinyRow(String label, JLabel val) {
        JPanel r = new JPanel(new BorderLayout(4,0));
        r.setBackground(CARD);
        r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(SUBTEXT);
        r.add(lbl, BorderLayout.WEST);
        r.add(val,  BorderLayout.EAST);
        return r;
    }

    String grade(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 75) return "A";
        if (pct >= 60) return "B";
        if (pct >= 45) return "C";
        return "F";
    }

    void alert(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    // ── Entry Point ───────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(StudentGradeManagerGUI::new);
    }
}
