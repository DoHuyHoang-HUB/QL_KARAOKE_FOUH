/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.toedter.calendar.JDateChooser;
import dao.HoaDon_DAO;
import entity.HoaDon;
import gui.swing.event.EventOnClick;
import gui.swing.graphics.ShadowType;
import gui.swing.panel.PanelShadow;
import gui.swing.table2.EventAction;
import gui.swing.textfield.MyComboBox;
import gui.swing.textfield.MyTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author NGUYE
 */
public class GD_HoaDon extends javax.swing.JPanel implements ActionListener{
    private HoaDon_DAO hoaDon_Dao;
    private List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
    private EventAction event;
    
    JCheckBox chkSapXepThuTu;
    JDateChooser dscBatDau, dscKetThuc;
    MyComboBox<String> cmbTuyChinh, cmbCot, cmbSapXep;
    MyTextField txtTimKiem;
    
    private PanelShadow panelHidden;
    private EventOnClick eventOnClick;
    
    public void addEvent(EventOnClick eventOnClick) {
        this.eventOnClick= eventOnClick;
    }
    /**
     * Creates new form GD_HoaDon
     */
    public GD_HoaDon() {
        hoaDon_Dao = new HoaDon_DAO();
        initComponents();
        loadData();
        build_GDHoaDon();
    }

    private void build_GDHoaDon() {
        createForm();
        createTable();
        setOpaque(false);
        setPreferredSize(new Dimension(getWidth(), 950));
        createPanelHidden();
        add(panelHidden);
    }
    
    private void createPanelHidden() {
        panelHidden = new PanelShadow();
        panelHidden.setShadowType(ShadowType.CENTER);
        panelHidden.setShadowOpacity(0.3f);
    }
    
    private void createForm(){
        String fontName = "sansserif";
        int fontPlain = Font.PLAIN;
        int font16 = 16;
        int font14 = 14;
        Color colorBtn = new Color(184, 238, 241);
        Color colorLabel = new Color(47, 72, 210);
        int separatorHeight = 150;
       
        pnlForm.setLayout(new MigLayout("", "[center][center][center]", "[center][center]"));
        pnlForm.add(createPanelTitle(), "span,pos 0al 0al 100% n, h 40!, wrap");
        /*
         * Begin: group Chọn thời gian 
         */
        JPanel pnlThoiGianHD = new JPanel();
        pnlThoiGianHD.setOpaque(false);

        pnlThoiGianHD.setLayout(new MigLayout("", "10[center] 10 [center]10", "60[][center]10[center]"));
        pnlForm.add(pnlThoiGianHD, "w 40%, h 200!");

        JLabel lblChonThoiGian = new JLabel("Chọn thời gian");
        lblChonThoiGian.setFont(new Font(fontName, fontPlain, font16));
        lblChonThoiGian.setForeground(colorLabel);
        pnlThoiGianHD.add(lblChonThoiGian, "span, w 100%, h 30!, wrap");

        // Chọn thời gian bắt đầu
        dscBatDau = new JDateChooser();
        dscBatDau.setOpaque(false);
        dscBatDau.setDateFormatString("yyyy-MM-dd");
        dscBatDau.setFont(new Font(fontName, fontPlain, font16));
        pnlThoiGianHD.add(dscBatDau, "w 50%, h 36!");

        // Chọn thời gian kết thúc
        dscKetThuc = new JDateChooser();
        dscKetThuc.setOpaque(false);
        
        //dscKetThuc.setDateFormatString("yyyy-MM-dd");
        dscKetThuc.setFont(new Font(fontName, fontPlain, font16));
        pnlThoiGianHD.add(dscKetThuc, "w 50%, h 36!, wrap");

        //Tùy chỉnh
        cmbTuyChinh = new MyComboBox<>();
        cmbTuyChinh.setFont(new Font(fontName, fontPlain, font16));
        cmbTuyChinh.setBorderLine(true);
        cmbTuyChinh.setBorderRadius(10);
        cmbTuyChinh.addItem("Tùy chỉnh");
        pnlThoiGianHD.add(cmbTuyChinh, "span, w 100%, h 36!");
        /*
         * End: group Chọn thời gian bắt đầu
         */

        JSeparator spr1 = new JSeparator(SwingConstants.VERTICAL);
        spr1.setPreferredSize(new Dimension(2, separatorHeight));
        pnlForm.add(spr1,"pos 0.4al 0.9al n n");
        
        /* 
         * Begin: group Tìm kiếm
         */
        JPanel pnlTimKiemHD = new JPanel();
        pnlTimKiemHD.setOpaque(false);
        /*
        Layout: 1 cột, 2 dòng
        cột 1, dòng 1: Ô nhập dữ liệu tìm kiếm
        cột 1, dòng 2: Chọn cột cần tìm
         */
        pnlTimKiemHD.setLayout(new MigLayout("", "[]10[center]10", "60[][center]10[center]"));
        pnlForm.add(pnlTimKiemHD, "w 40%, h 200!");

        JLabel lblTimKiem = new JLabel("Tìm kiếm");
        lblTimKiem.setFont(new Font(fontName, fontPlain, font16));
        lblTimKiem.setForeground(colorLabel);
        pnlTimKiemHD.add(lblTimKiem, "span, w 100%, h 30!, wrap");

        //Chọn cột cần tìm
        cmbCot = new MyComboBox<>(new Object[]{"Chọn cột cần tìm","Mã hóa đơn","Khách hàng","Phòng"});
        cmbCot.setFont(new Font(fontName, fontPlain, font16));
        cmbCot.setBorderLine(true);
        cmbCot.setBorderRadius(10);
        //cmbCot.addItem("Chọn cột cần tìm");
        pnlTimKiemHD.add(cmbCot, "w 100%, h 36!, wrap");
        
        // Tìm kiếm  
        txtTimKiem = new MyTextField();
        txtTimKiem.setFont(new Font(fontName, fontPlain, font16));
        txtTimKiem.setBorderLine(true);
        txtTimKiem.setBorderRadius(5);
        pnlTimKiemHD.add(txtTimKiem, "w 100%, h 36!");

        /*
         * End: group Tìm kiếm
         */
        JSeparator spr2 = new JSeparator(SwingConstants.VERTICAL);
        spr2.setPreferredSize(new Dimension(2, separatorHeight));
        pnlForm.add(spr2,"pos 0.8al 0.9al n n");
        
        /*
         * Begin: group Sắp xếp
         */
        JPanel pnlSapXepHD = new JPanel();
        pnlSapXepHD.setOpaque(false);

        pnlSapXepHD.setLayout(new MigLayout("", "10[][]10", "60[][center]10[center]10"));
        pnlForm.add(pnlSapXepHD, "w 20%, h 200!");
        
        JLabel lblSapXep = new JLabel("Sắp xếp");
        lblSapXep.setFont(new Font(fontName, fontPlain, font16));
        lblSapXep.setForeground(colorLabel);
        pnlSapXepHD.add(lblSapXep, "span, w 100%, h 30!, wrap");

        //Chọn cột cần sắp xếp  
        cmbSapXep = new MyComboBox<>(new Object[]{"Tất cả","Đơn giá phòng","Số giờ hát","Tổng hóa đơn","Tổng tiền mặt hàng"});
        cmbSapXep.setFont(new Font(fontName, fontPlain, font16));
        cmbSapXep.setBorderLine(true);
        cmbSapXep.setBorderRadius(10);
        pnlSapXepHD.add(cmbSapXep, "span, w 100%, h 36!, wrap");

        //Sắp xếp từ bé đến lớn
        JPanel pnlSapXepThuTu = new JPanel();
        pnlSapXepThuTu.setOpaque(false);
        pnlSapXepThuTu.setLayout(new MigLayout("", "push[]0[]0", "[]"));

        chkSapXepThuTu = new JCheckBox();
        chkSapXepThuTu.setOpaque(false);
        pnlSapXepThuTu.add(chkSapXepThuTu);

        JLabel lblSapXepThuTu = new JLabel("Bé đến lớn");
        lblSapXepThuTu.setFont(new Font(fontName, fontPlain, font16));
        pnlSapXepThuTu.add(lblSapXepThuTu);

        pnlSapXepHD.add(pnlSapXepThuTu, "w 100%"); 
        
        /*Đăng ký sự kiện*/
        cmbSapXep.addActionListener(this);

        xuLySuKien();
    }
    
    private JPanel createPanelTitle() {
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 0.1f)));
        pnlTitle.setOpaque(false);
        pnlTitle.setLayout(new MigLayout("fill", "", ""));
        JLabel lblTitle = new JLabel();
        lblTitle.setText("QUẢN LÝ HÓA ĐƠN");
        lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));
        lblTitle.setForeground(new Color(68, 68, 68));
        pnlTitle.add(lblTitle);
        return  pnlTitle;
    }
    
    private void createTable(){
        tblHoaDon.fixTable(scrHoaDon);
       
    }
    
    public void xoaDuLieu(){
        DefaultTableModel df = (DefaultTableModel) tblHoaDon.getModel();
        df.setRowCount(0);
    }
    
    public void taiLaiDuLieu(List<HoaDon> dsHoaDon){
        dsHoaDon.forEach((hoaDon)->{
            tblHoaDon.addRow(new Object[]{hoaDon.getMaHoaDon(), hoaDon.getKhachHang().getTenKhachHang(), hoaDon.getPhong().getTenPhong(), hoaDon.getGioHat(), hoaDon.getNgayLapHoaDon(), hoaDon.getThoiGianBatDau(), hoaDon.getThoiGianKetThuc(), hoaDon.getTongTienMatHang(), hoaDon.getDonGiaPhong(), hoaDon.getTongHoaDon(), hoaDon.getNhanVien().getTenNhanVien()});
        });
    }
    
    private void loadData() {
        dsHoaDon = hoaDon_Dao.getDsHoaDon();
        dsHoaDon.forEach((hoaDon)->{
            tblHoaDon.addRow(new Object[]{hoaDon.getMaHoaDon(), hoaDon.getKhachHang().getTenKhachHang(), hoaDon.getPhong().getTenPhong(), hoaDon.getGioHat(), hoaDon.getNgayLapHoaDon(), hoaDon.getThoiGianBatDau(), hoaDon.getThoiGianKetThuc(), hoaDon.getTongTienMatHang(), hoaDon.getDonGiaPhong(), hoaDon.getTongHoaDon(), hoaDon.getNhanVien().getTenNhanVien()});
        });
    }

    private void xuLySuKien(){
        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //Nếu click chuột trái và click 2 lần
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    int row = tblHoaDon.getSelectedRow();
                    String maHoaDon = tblHoaDon.getValueAt(row, 0).toString();
                    System.out.println(hoaDon_Dao.getHoaDon(maHoaDon));
                    eventOnClick.onClick(hoaDon_Dao.getHoaDon(maHoaDon));
                }
            }
        });
        
        chkSapXepThuTu.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange()==1){
                            String tieuChi = traVeTieuChiSapXep(cmbSapXep.getSelectedItem().toString());
                            dsHoaDon = hoaDon_Dao.sapXepTheo(tieuChi, "asc");
                            xoaDuLieu();
                            taiLaiDuLieu(dsHoaDon);
                        }else{
                            String tieuChi = traVeTieuChiSapXep(cmbSapXep.getSelectedItem().toString());
                            dsHoaDon = hoaDon_Dao.sapXepTheo(tieuChi, "desc");
                            xoaDuLieu();
                            taiLaiDuLieu(dsHoaDon);
                        }
                    }
                });
        
        txtTimKiem.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                if(cmbCot.getSelectedIndex()!=0){
                    String tk;
                    String s = txtTimKiem.getText().trim();
                    String tieuChi = cmbCot.getSelectedItem().toString();
                   switch(tieuChi){
                        case "Mã hóa đơn":
                            tk = "maHoaDon";
                            dsHoaDon = hoaDon_Dao.getDSHoaDonByTieuChiKhac(tk, s);
                            break;
                        case "Khách hàng":
                            dsHoaDon = hoaDon_Dao.getDSHoaDonByTenKhachHang(s);
                            break;
                        case "Phòng":
                            dsHoaDon= hoaDon_Dao.getDSHoaDonByTenPhong(s);
                            break;
                        default:
                            dsHoaDon = hoaDon_Dao.getDsHoaDon();
                            break;
                    }
                   xoaDuLieu();
                   taiLaiDuLieu(dsHoaDon);
                }else{
                    dsHoaDon = hoaDon_Dao.getDsHoaDon();
                    xoaDuLieu();
                    taiLaiDuLieu(dsHoaDon);
                }
            }
        });

                      
        dscBatDau.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
           public void propertyChange(PropertyChangeEvent arg0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if(dscBatDau.getDate()!=null){
                    System.out.println(df.format(dscBatDau.getDate()));
                    if(dscKetThuc.getDate()==null){
                        dsHoaDon = hoaDon_Dao.getDSHoaDonAfter_Date(df.format(dscBatDau.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }else{
                        dsHoaDon = hoaDon_Dao.getDSHoaDonFromDateToDate(df.format(dscBatDau.getDate()), df.format(dscKetThuc.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }
                }else{
                    if(dscKetThuc.getDate()==null){
                        xoaDuLieu();
                        loadData();
                    }else{
                        dsHoaDon = hoaDon_Dao.getDSHoaDonBefore_Date(df.format(dscKetThuc.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }
                }

            }
        });
        
        dscKetThuc.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if(dscKetThuc.getDate()!=null){
                    if(dscBatDau.getDate()==null){
                        dsHoaDon = hoaDon_Dao.getDSHoaDonBefore_Date(df.format(dscKetThuc.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }else{
                        dsHoaDon = hoaDon_Dao.getDSHoaDonFromDateToDate(df.format(dscBatDau.getDate()), df.format(dscKetThuc.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }
                }else{
                    if(dscBatDau.getDate()==null){
                        xoaDuLieu();
                        loadData();
                    }else{
                        dsHoaDon = hoaDon_Dao.getDSHoaDonFromDateToDate(df.format(dscBatDau.getDate()), df.format(dscKetThuc.getDate()));
                        xoaDuLieu();
                        taiLaiDuLieu(dsHoaDon);
                    }
                }
            }
        });
    }
    
    public String traVeTieuChiSapXep(String tieuChi){
        String s;
        switch(tieuChi){
            case "Đơn giá phòng":
                s = "donGiaPhong";
                break;
            case "Số giờ hát":
                s = "gioHat";
                break;
            case "Tổng hóa đơn":
                s = "tongHoaDon";
                break;
            case "Tổng tiền mặt hàng":
                s = "tongTienMatHang";
                break;
            default:
                s = "ngayLapHoaDon";
                break;
        }
        return s;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlForm = new gui.swing.panel.PanelShadow();
        pnlCenter = new gui.swing.panel.PanelShadow();
        scrHoaDon = new javax.swing.JScrollPane();
        tblHoaDon = new gui.swing.table2.MyTable();
        lblBang = new javax.swing.JLabel();

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setShadowOpacity(0.3F);
        pnlForm.setShadowSize(3);

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1079, Short.MAX_VALUE)
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );

        pnlCenter.setBackground(new java.awt.Color(255, 255, 255));
        pnlCenter.setShadowOpacity(0.3F);
        pnlCenter.setShadowSize(3);

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Hóa Đơn", "Khách Hàng", "Phòng", "Số Giờ Hát", "Ngày Lập", "Giờ Bắt Đầu", "Giờ Kết Thúc", "Tổng Mặt Hàng", "Giá Phòng", "Tổng Hóa Đơn", "Nhân Viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        scrHoaDon.setViewportView(tblHoaDon);

        lblBang.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lblBang.setForeground(new java.awt.Color(4, 72, 210));
        lblBang.setText("   Danh Sách Hóa Đơn");
        lblBang.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrHoaDon)
            .addComponent(lblBang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addComponent(lblBang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCenter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblBang;
    private gui.swing.panel.PanelShadow pnlCenter;
    private gui.swing.panel.PanelShadow pnlForm;
    private javax.swing.JScrollPane scrHoaDon;
    private gui.swing.table2.MyTable tblHoaDon;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj.equals(cmbSapXep)){
            if(cmbSapXep.getSelectedIndex()!=0){
                String tieuChi = cmbSapXep.getSelectedItem().toString();
                String s = traVeTieuChiSapXep(tieuChi);
                if(chkSapXepThuTu.isEnabled()==true){
                    dsHoaDon = hoaDon_Dao.sapXepTheo(s, "desc");
                    xoaDuLieu();
                    taiLaiDuLieu(dsHoaDon);
                }else{
                    dsHoaDon = hoaDon_Dao.sapXepTheo(s, "desc");
                    xoaDuLieu();
                    taiLaiDuLieu(dsHoaDon);
                }
            }else{
                dsHoaDon = hoaDon_Dao.getDsHoaDon();
                xoaDuLieu();
                taiLaiDuLieu(dsHoaDon);
            }
        }
    }
}
