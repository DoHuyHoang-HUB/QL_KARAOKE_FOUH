/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.toedter.calendar.JDateChooser;
import gui.swing.textfield.MyComboBox;
import gui.swing.textfield.MyTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author NGUYE
 */
public class GD_HoaDon extends javax.swing.JPanel {

    /**
     * Creates new form GD_HoaDon
     */
    public GD_HoaDon() {
        initComponents();
        build_GDHoaDon();
    }

    private void build_GDHoaDon() {
        String fontName = "sansserif";
        int fontPlain = Font.PLAIN;
        int font16 = 16;
        int font14 = 14;
        Color colorBtn = new Color(184, 238, 241);
        Color colorLabel = new Color(47, 72, 210);
        int separatorHeight = 110;

        lblMenu.setFont(new Font(fontName, fontPlain, font16));

        /*
        Chiều cao pnlForm là 141, chiều cao pnlThoiGianHD(có borderTitle) là 135
        -> chênh lệch là 6(border shadow)
        Tương tự cho pnlTimKiemHD và pnlSapXepHD
        */
        /*
        Layout: 3 cột, 1 dòng
        cột 1, dòng 1: group Chọn thời gian
        cột 2, dòng 1: group Tìm kiếm
        cột 3, dòng 1: group Sắp xếp
         */
        pnlForm.setLayout(new MigLayout("", "[][][][][]", "[]"));

        /*
         * Begin: group Chọn thời gian 
         */
        JPanel pnlThoiGianHD = new JPanel();
        pnlThoiGianHD.setOpaque(false);
//        pnlThoiGianHD.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Chọn thời gian", TitledBorder.LEFT, TitledBorder.TOP, new Font("sansserif", Font.PLAIN, 18), Color.gray));

        /*
        Layout: 2 cột, 2 dòng
        cột 1, dòng 1: Ngày bắt đầu
        cột 2, dòng 1: Ngày bắt đầu
        cột 1+2, dòng 2: Tùy chỉnh
         */
        pnlThoiGianHD.setLayout(new MigLayout("", "10[center] 10 [center]10", "[][center]10[center]"));
        pnlForm.add(pnlThoiGianHD, "w 40%, h 135!");

        JLabel lblChonThoiGian = new JLabel("Chọn thời gian");
        lblChonThoiGian.setFont(new Font(fontName, fontPlain, font16));
        lblChonThoiGian.setForeground(colorLabel);
        pnlThoiGianHD.add(lblChonThoiGian, "span, w 100%, h 30!, wrap");

        // Chọn thời gian bắt đầu
        JDateChooser dscBatDau = new JDateChooser();
        dscBatDau.setFont(new Font(fontName, fontPlain, font16));
        pnlThoiGianHD.add(dscBatDau, "w 50%, h 36!");

        // Chọn thời gian kết thúc
        JDateChooser dscKetThuc = new JDateChooser();
        dscKetThuc.setFont(new Font(fontName, fontPlain, font16));
        pnlThoiGianHD.add(dscKetThuc, "w 50%, h 36!, wrap");

        //Tùy chỉnh
        MyComboBox<String> cmbTuyChinh = new MyComboBox<>();
        cmbTuyChinh.setFont(new Font(fontName, fontPlain, font16));
        cmbTuyChinh.setBorderLine(true);
        cmbTuyChinh.addItem("Tùy chỉnh");
        pnlThoiGianHD.add(cmbTuyChinh, "span, w 100%, h 36!");
        /*
         * End: group Chọn thời gian bắt đầu
         */

        JSeparator spr1 = new JSeparator(SwingConstants.VERTICAL);
        spr1.setPreferredSize(new Dimension(2, separatorHeight));
        pnlForm.add(spr1);
        
        /* 
         * Begin: group Tìm kiếm
         */
        JPanel pnlTimKiemHD = new JPanel();
        pnlTimKiemHD.setOpaque(false);
//        pnlTimKiemHD.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP, new Font("sansserif", Font.PLAIN, 18), Color.gray));
        /*
        Layout: 1 cột, 2 dòng
        cột 1, dòng 1: Ô nhập dữ liệu tìm kiếm
        cột 1, dòng 2: Chọn cột cần tìm
         */
        pnlTimKiemHD.setLayout(new MigLayout("", "[]10[center]10", "[][center]10[center]"));
        pnlForm.add(pnlTimKiemHD, "w 40%, h 135!");

        JLabel lblTimKiem = new JLabel("Tìm kiếm");
        lblTimKiem.setFont(new Font(fontName, fontPlain, font16));
        lblTimKiem.setForeground(colorLabel);
        pnlTimKiemHD.add(lblTimKiem, "span, w 100%, h 30!, wrap");

        // Tìm kiếm
        MyTextField txtTimKiem = new MyTextField();
        txtTimKiem.setFont(new Font(fontName, fontPlain, font16));
        pnlTimKiemHD.add(txtTimKiem, "w 100%, h 36!, wrap");

        //Chọn cột cần tìm
        MyComboBox<String> cmbCot = new MyComboBox<>();
        cmbCot.setFont(new Font(fontName, fontPlain, font16));
        cmbCot.setBorderLine(true);
        cmbCot.addItem("Chọn cột cần tìm");
        pnlTimKiemHD.add(cmbCot, "w 100%, h 36!");

        /*
         * End: group Tìm kiếm
         */
        JSeparator spr2 = new JSeparator(SwingConstants.VERTICAL);
        spr2.setPreferredSize(new Dimension(2, separatorHeight));
        pnlForm.add(spr2);
        
        /*
         * Begin: group Sắp xếp
         */
        JPanel pnlSapXepHD = new JPanel();
        pnlSapXepHD.setOpaque(false);
//        pnlSapXepHD.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Sắp xếp", TitledBorder.LEFT, TitledBorder.TOP, new Font("sansserif", Font.PLAIN, 18), Color.gray));
        /*
        Layout: 1 cột, 2 dòng
        cột 1, dòng 1: Chọn cột cần sắp xếp
        cột 1, dòng 2: Sắp xếp từ bé đến lớn
         */
        pnlSapXepHD.setLayout(new MigLayout("", "10[][]10", "[][center]10[center]10"));
        pnlForm.add(pnlSapXepHD, "w 20%, h 135!");
        
        JLabel lblSapXep = new JLabel("Sắp xếp");
        lblSapXep.setFont(new Font(fontName, fontPlain, font16));
        lblSapXep.setForeground(colorLabel);
        pnlSapXepHD.add(lblSapXep, "span, w 100%, h 30!, wrap");

        //Chọn cột cần sắp xếp
        MyComboBox<String> cmbSapXep = new MyComboBox<>();
        cmbSapXep.setFont(new Font(fontName, fontPlain, font16));
        cmbSapXep.setBorderLine(true);
        cmbSapXep.addItem("Tất cả");
        pnlSapXepHD.add(cmbSapXep, "span, w 100%, h 36!, wrap");

        //Sắp xếp từ bé đến lớn
        JPanel pnlSapXepThuTu = new JPanel();
        pnlSapXepThuTu.setOpaque(false);
        /*
        Layout: 2 cột, 1 dòng
        cột 1, dòng 1: Checkbox
        cột 2, dòng 1: label
         */
        pnlSapXepThuTu.setLayout(new MigLayout("", "push[]0[]0", "[]"));

        JCheckBox chkSapXepThuTu = new JCheckBox();
        chkSapXepThuTu.setOpaque(false);
        pnlSapXepThuTu.add(chkSapXepThuTu);

        JLabel lblSapXepThuTu = new JLabel("Bé đến lớn");
        lblSapXepThuTu.setFont(new Font(fontName, fontPlain, font16));
        pnlSapXepThuTu.add(lblSapXepThuTu);

        pnlSapXepHD.add(pnlSapXepThuTu, "w 100%");

        /*
         * End: group Sắp xếp
         */
        setPreferredSize(new Dimension(getWidth(), 1500));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblMenu = new javax.swing.JLabel();
        pnlForm = new gui.panel.PanelShadow();
        pnlCenter = new gui.panel.PanelShadow();

        lblMenu.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblMenu.setForeground(new java.awt.Color(4, 72, 210));
        lblMenu.setText("Quản lý hóa đơn ");

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setShadowOpacity(0.3F);
        pnlForm.setShadowSize(3);
        pnlForm.setShadowType(gui.dropshadow.ShadowType.TOP);

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        pnlCenter.setBackground(new java.awt.Color(255, 255, 255));
        pnlCenter.setShadowOpacity(0.3F);
        pnlCenter.setShadowSize(3);
        pnlCenter.setShadowType(gui.dropshadow.ShadowType.TOP);

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 493, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblMenu)
                .addGap(0, 986, Short.MAX_VALUE))
            .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblMenu;
    private gui.panel.PanelShadow pnlCenter;
    private gui.panel.PanelShadow pnlForm;
    // End of variables declaration//GEN-END:variables
}
