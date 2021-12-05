package gui;

import com.toedter.calendar.JDateChooser;
import dao.HoaDon_DAO;
import dao.NhanVien_DAO;
import entity.HoaDon;
import gui.swing.button.Button;
import gui.swing.event.EventSelectedRow;
import gui.swing.graphics.ShadowType;
import gui.swing.panel.PanelShadow;
import gui.swing.panel.slideshow.EventPagination;
import gui.swing.table2.EventAction;
import gui.swing.textfield.MyTextFieldFlatlaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.miginfocom.swing.MigLayout;

public class GD_HoaDon extends javax.swing.JPanel implements ActionListener {
    
    private HoaDon_DAO hoaDon_Dao;
    private List<HoaDon> dsHoaDon = new ArrayList<HoaDon>();
    private EventAction event;

    JCheckBox chkSapXepThuTu;
    JDateChooser dscBatDau, dscKetThuc;
    JComboBox<String> cmbTuyChinh, cmbCot, cmbSapXep;
    MyTextFieldFlatlaf txtTimKiem;

    private PanelShadow panelHidden;

    private JComboBox<Object> cmbQuy;
    private JComboBox<Object> cmbThang;
    private JComboBox<Object> cmbNam;
    private Button btnLamMoi;
    private List<Integer> dsThang, dsQuy, dsNam;

    private EventSelectedRow eventOnClick;
    private NhanVien_DAO nhanVien_Dao;
    
    public void addEvent(EventSelectedRow eventOnClick) {
        this.eventOnClick = eventOnClick;
    }

    public GD_HoaDon() {
        hoaDon_Dao = new HoaDon_DAO();
        nhanVien_Dao = new NhanVien_DAO();
        initComponents();
        loadData(pnlPage.getCurrentIndex());
        build_GDHoaDon();
    }
    
    private void build_GDHoaDon() {
        createForm();
        createTable();
        createPanelBottom();
        loadThangLenCombobox(dsThang);
        loadQuyLenCombobox(dsQuy);
        loadNamLenCombobox(dsNam);
        setOpaque(false);
    }
    
    private void createForm() {
        String fontName = "sansserif";
        int fontPlain = Font.PLAIN;
        int font16 = 16;
        int font14 = 14;
        Color colorBtn = new Color(184, 238, 241);
        Color colorLabel = new Color(47, 72, 210);
       
        pnlForm.setLayout(new MigLayout("fillx, insets 0, wrap", "[fill][fill]", "40[][]"));
        pnlForm.add(createPanelTitle(), "pos 0al 0al 100% n, h 40!");
        /*
         * Begin: group Chọn thời gian 
         */
        JPanel pnlThoiGianHD = new JPanel();
        pnlThoiGianHD.setOpaque(false);
        pnlThoiGianHD.setBorder(new MatteBorder(0, 0, 0, 1, new Color(0, 0, 0, 0.1f)));

        pnlThoiGianHD.setLayout(new MigLayout("wrap, insets 0", "10[][fill][][fill]10", "8[][]10[]"));
        pnlForm.add(pnlThoiGianHD, " w 55%");

        JLabel lblChonThoiGian = new JLabel("Tìm kiếm theo thời gian");
        lblChonThoiGian.setFont(new Font(fontName, fontPlain, font14));
        lblChonThoiGian.setForeground(colorLabel);
        pnlThoiGianHD.add(lblChonThoiGian, "span 4");
        JLabel lblTu;

        // Chọn thời gian bắt đầu
        pnlThoiGianHD.add(lblTu = new JLabel("Từ: "));
        lblTu.setFont(new Font(fontName, fontPlain, font14));
        dscBatDau = new JDateChooser();
        dscBatDau.setOpaque(false);
        dscBatDau.setDateFormatString("yyyy-MM-dd");
        dscBatDau.setFont(new Font(fontName, fontPlain, font14));
        pnlThoiGianHD.add(dscBatDau, "w 40%, h 30!");
        JLabel lblDen;

        // Chọn thời gian kết thúc
        pnlThoiGianHD.add(lblDen = new JLabel("Đến: "));
        lblDen.setFont(new Font(fontName, fontPlain, font14));
        dscKetThuc = new JDateChooser();
        dscKetThuc.setOpaque(false);
        dscKetThuc.setDateFormatString("yyyy-MM-dd");
        dscKetThuc.setFont(new Font(fontName, fontPlain, font14));
        pnlThoiGianHD.add(dscKetThuc, "w 40%, h 30!");

        JPanel pnlCmbThoiGian = new JPanel(new MigLayout("", "0[center]push[center]push[center]0", "[][]"));
        pnlCmbThoiGian.setBackground(Color.WHITE);
        pnlThoiGianHD.add(pnlCmbThoiGian, "span 4, w 100%");
        
        cmbNam = new JComboBox<>();
        cmbNam.setFont(new Font(fontName, fontPlain, font14));
        cmbNam.addItem("Lọc theo năm");
        pnlCmbThoiGian.add(cmbNam, "w 32%, h 30!");
        
     //Tùy chỉnh
        cmbQuy = new JComboBox<>();
        cmbQuy.setFont(new Font(fontName, fontPlain, font14));
        cmbQuy.addItem("Lọc theo quý");
        pnlCmbThoiGian.add(cmbQuy, "w 32%, h 30!");
        
        cmbThang = new JComboBox<>();
        cmbThang.setFont(new Font(fontName, fontPlain, font14));
        cmbThang.addItem("Lọc theo tháng");
        pnlCmbThoiGian.add(cmbThang, "w 32%, h 30!");
        /*
         * End: group Chọn thời gian bắt đầu
         */

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
        pnlTimKiemHD.setLayout(new MigLayout("wrap, insets 0", "[]10[]10", "[][]10[]"));
        pnlForm.add(pnlTimKiemHD);

        JLabel lblTimKiem = new JLabel("Tìm kiếm cụ thể");
        lblTimKiem.setFont(new Font(fontName, fontPlain, font14));
        lblTimKiem.setForeground(colorLabel);
        pnlTimKiemHD.add(lblTimKiem, "span 2");

        //Chọn cột cần tìm
        cmbCot = new JComboBox<>(new String[]{"Chọn cột cần tìm","Mã hóa đơn","Khách hàng","Phòng"});
        cmbCot.setFont(new Font(fontName, fontPlain, font14));
        cmbCot.addItem("Chọn cột cần tìm");
        pnlTimKiemHD.add(cmbCot, "span 2, h 30!, w 100%");
        

        // Tìm kiếm  
        txtTimKiem = new MyTextFieldFlatlaf();
        txtTimKiem.setFont(new Font(fontName, fontPlain, font14));
        txtTimKiem.setHint("Nhập thông tin tìm kiếm theo tùy chọn của bạn.");
        pnlTimKiemHD.add(txtTimKiem, "h 30!, w 100%");

        // Nút Làm mới
        btnLamMoi = new Button("Làm mới");
        btnLamMoi.setFont(new Font(fontName, fontPlain, font14));
        btnLamMoi.setBackground(colorBtn);
        btnLamMoi.setBorderRadius(5);
        btnLamMoi.setBorderline(true);
        pnlTimKiemHD.add(btnLamMoi, "w 100!, h 36!");

        /*Đăng ký sự kiện*/
    }

    private JPanel createPanelTitle() {
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 0.1f)));
        pnlTitle.setOpaque(false);
        pnlTitle.setLayout(new MigLayout("fill", "", ""));
        JLabel lblTitle = new JLabel();
        lblTitle.setText("Quản lý hóa đơn");
        lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));
        lblTitle.setForeground(new Color(68, 68, 68));
        pnlTitle.add(lblTitle);
        return pnlTitle;
    }
    
    private void createTable(){
        tblHoaDon.getTableHeader().setFont(new Font("Sansserif", Font.BOLD, 14));
        tblHoaDon.getTableHeader().setFont(new Font("sansserif", Font.BOLD, 14));
        RowSorter<TableModel> sorter = new TableRowSorter<>((DefaultTableModel) tblHoaDon.getModel());
        tblHoaDon.setRowSorter(sorter);
        xuLySuKien();
    }
    
    public void xoaDuLieu() {
        DefaultTableModel df = (DefaultTableModel) tblHoaDon.getModel();
        df.setRowCount(0);
    }
    
    public void taiLaiDuLieu(List<HoaDon> dsHoaDon){
        ((DefaultTableModel) tblHoaDon.getModel()).setRowCount(0);
        DecimalFormat dcf = new DecimalFormat("#,###");
        SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fm2 = new SimpleDateFormat("HH:mm");
        List<HoaDon> dsTam = xuLyLoai(dsHoaDon);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (dsTam != null) {
                    for(HoaDon hoaDon: dsTam){
                        ((DefaultTableModel) tblHoaDon.getModel()).addRow(new Object[]{hoaDon.getMaHoaDon(), hoaDon.getKhachHang().getTenKhachHang(), hoaDon.getPhong().getTenPhong(), hoaDon.getGioHat(), fm1.format(hoaDon.getNgayLapHoaDon()), fm2.format(hoaDon.getThoiGianBatDau()), dcf.format(hoaDon.getTongTienMatHang()), dcf.format(hoaDon.getPhong().getLoaiPhong().getGiaPhong()), dcf.format(hoaDon.getTongHoaDon()), hoaDon.getNhanVien().getTenNhanVien()});
                    }
                }
                tblHoaDon.repaint();
                tblHoaDon.revalidate();
            }
        }).start();
    }
    
    public List<HoaDon> xuLyLoai(List<HoaDon> dsHoaDon){
        List<HoaDon> dsTam = new ArrayList<>();
        if(nhanVien_Dao.getMaNhanVienQuanLy().contains(GD_Chinh.NHAN_VIEN.getMaNhanVien())){
            return dsHoaDon;
        }else{
            for(HoaDon hoaDon: dsHoaDon){
                if(hoaDon.getNhanVien().getMaNhanVien().equals(GD_Chinh.NHAN_VIEN.getMaNhanVien())){
                    dsTam.add(hoaDon);
                }
            }
            return dsTam;
        }
    }    
    private void loadData(int numPage) {
        ((DefaultTableModel) tblHoaDon.getModel()).setRowCount(0);
        DecimalFormat dcf = new DecimalFormat("#,###");
        SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fm2 = new SimpleDateFormat("HH:mm");
        dsHoaDon = hoaDon_Dao.getDsHoaDon(numPage);
        if(dsHoaDon != null) {
            List<HoaDon> dsTam = xuLyLoai(dsHoaDon);
            for(HoaDon hoaDon: dsTam){
                ((DefaultTableModel) tblHoaDon.getModel()).addRow(new Object[]{hoaDon.getMaHoaDon(), hoaDon.getKhachHang().getTenKhachHang(), hoaDon.getPhong().getTenPhong(), hoaDon.getGioHat(), fm1.format(hoaDon.getNgayLapHoaDon()), fm2.format(hoaDon.getThoiGianBatDau()), dcf.format(hoaDon.getTongTienMatHang()), dcf.format(hoaDon.getPhong().getLoaiPhong().getGiaPhong()), dcf.format(hoaDon.getTongHoaDon()), hoaDon.getNhanVien().getTenNhanVien()});
            }
        }
        tblHoaDon.repaint();
        tblHoaDon.revalidate();
    }
    
    private void loadPage(int soLuongPhieu) {
        pnlPage.init(soLuongPhieu% 20 == 0 ? soLuongPhieu / 20 : (soLuongPhieu / 20) + 1);
    }
    
    private void createPanelBottom() {
        pnlPage.addEventPagination(new EventPagination() {
            @Override
            public void onClick(int pageClick) {
                loadData(pageClick);
            }
        });
        loadPage(hoaDon_Dao.getSoLuongHoaDon());
    }

    private void xuLySuKien(){
        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //Nếu click chuột trái 2 lần
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    int row = tblHoaDon.getSelectedRow();
                    String maHoaDon = tblHoaDon.getValueAt(row, 0).toString();
                    System.out.println(hoaDon_Dao.getHoaDon(maHoaDon));
                    eventOnClick.selectedRow(hoaDon_Dao.getHoaDon(maHoaDon));
                }
            }
        });
        
        txtTimKiem.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
            }
            @Override
            public void keyPressed(KeyEvent arg0) {
                
                if(cmbCot.getSelectedIndex()==0){
                    JOptionPane.showMessageDialog(GD_HoaDon.this, "Hãy chọn cột mà bạn muốn tìm kiếm.");
                    txtTimKiem.setText("");
                    txtTimKiem.requestFocus();
                }
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
                            dsHoaDon = hoaDon_Dao.getDSHoaDonByTieuChiKhac(tk, s, pnlPage.getCurrentIndex());
                            xoaDuLieu();
                            loadPage(hoaDon_Dao.getSoLuongHoaDonByTieuChiKhac(tk, s));
                            break;
                        case "Khách hàng":
                            dsHoaDon = hoaDon_Dao.getDSHoaDonByTenKhachHang(s, pnlPage.getCurrentIndex());
                            xoaDuLieu();
                            loadPage(hoaDon_Dao.getSoLuongHoaDonByTenKhachHang(s));
                            break;
                        case "Phòng":
                            dsHoaDon= hoaDon_Dao.getDSHoaDonByTenPhong(s, pnlPage.getCurrentIndex());
                            xoaDuLieu();
                            loadPage(hoaDon_Dao.getSoLuongHoaDonByTenPhong(s));
                            break;
                        default:
                            dsHoaDon = hoaDon_Dao.getDsHoaDon(pnlPage.getCurrentIndex());
                            xoaDuLieu();
                            loadPage(hoaDon_Dao.getSoLuongHoaDon());
                            break;
                    }
                   taiLaiDuLieu(dsHoaDon);
                }else{
                    dsHoaDon = hoaDon_Dao.getDsHoaDon(pnlPage.getCurrentIndex());
                    xoaDuLieu();
                    loadPage(hoaDon_Dao.getSoLuongHoaDon());
                    taiLaiDuLieu(dsHoaDon);
                }
            }
        });
                      
        dscBatDau.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
           public void propertyChange(PropertyChangeEvent arg0) {
                String to = kiemTraNgayKetThuc();
                String from = kiemTraNgayBatDau();
                dsHoaDon = hoaDon_Dao.getDSHoaDonFromDateToDate(from, to, pnlPage.getCurrentIndex());
                xoaDuLieu();
                loadPage(hoaDon_Dao.getSoLuongHoaDonFromDateToDate(from, to));
                taiLaiDuLieu(dsHoaDon);
            }
        });
        
        dscKetThuc.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                String to = kiemTraNgayKetThuc();
                String from = kiemTraNgayBatDau();
                dsHoaDon = hoaDon_Dao.getDSHoaDonFromDateToDate(from, to, pnlPage.getCurrentIndex());
                xoaDuLieu();
                loadPage(hoaDon_Dao.getSoLuongHoaDonFromDateToDate(from, to));
                taiLaiDuLieu(dsHoaDon);
            }
        });
    }
    
    public void loadThangLenCombobox(List<Integer> dsThang){
        dsThang = hoaDon_Dao.getDSThangTheoNgayLap();
        dsThang.forEach(t->{
            cmbThang.addItem(t);
        });
    }
    
    public void loadQuyLenCombobox(List<Integer> dsQuy){
        dsQuy = hoaDon_Dao.getDSQuyTheoNgayLap();
        dsQuy.forEach(t->{
            cmbQuy.addItem(t);
        });
    }
    
    public void loadNamLenCombobox(List<Integer> dsNam){
        dsNam = hoaDon_Dao.getDSNamTheoNgayLap();
        dsNam.forEach(t->{
            cmbNam.addItem(t);
        });
    }
    
    public String kiemTraNgayBatDau(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String from;
        if(dscBatDau.getDate()==null){
            from = hoaDon_Dao.layNgayLapNhoNhat();
        }else{
            from = df.format(dscBatDau.getDate());
        }
        return from;
    }
    
    public String kiemTraNgayKetThuc(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String to;
        if(dscKetThuc.getDate()==null){
            to = hoaDon_Dao.layNgayLapLonNhat();
        }else{
            to = df.format(dscKetThuc.getDate());
        }
        return to;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlForm = new gui.swing.panel.PanelShadow();
        panelShadow2 = new gui.swing.panel.PanelShadow();
        pnlBottom_Page = new javax.swing.JPanel();
        pnlPage = new gui.swing.table2.PanelPage();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDon = new gui.swing.table2.MyTableFlatlaf();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout(0, 5));

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setShadowOpacity(0.3F);
        pnlForm.setShadowSize(2);
        pnlForm.setShadowType(gui.swing.graphics.ShadowType.TOP);

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        add(pnlForm, java.awt.BorderLayout.PAGE_START);

        panelShadow2.setBackground(new java.awt.Color(255, 255, 255));
        panelShadow2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelShadow2.setShadowOpacity(0.3F);
        panelShadow2.setShadowSize(2);
        panelShadow2.setShadowType(gui.swing.graphics.ShadowType.TOP);
        panelShadow2.setLayout(new java.awt.BorderLayout());

        pnlBottom_Page.setOpaque(false);

        pnlPage.setOpaque(false);

        javax.swing.GroupLayout pnlBottom_PageLayout = new javax.swing.GroupLayout(pnlBottom_Page);
        pnlBottom_Page.setLayout(pnlBottom_PageLayout);
        pnlBottom_PageLayout.setHorizontalGroup(
            pnlBottom_PageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBottom_PageLayout.createSequentialGroup()
                .addComponent(pnlPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 862, Short.MAX_VALUE))
        );
        pnlBottom_PageLayout.setVerticalGroup(
            pnlBottom_PageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPage, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        panelShadow2.add(pnlBottom_Page, java.awt.BorderLayout.PAGE_END);

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Khách hàng", "Phòng", "Giờ hát", "Ngày lập", "Giờ bắt đầu", "Tổng mặt hàng", "Giá phòng", "Tổng hóa đơn", "Nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblHoaDon.setRowHeight(40);
        tblHoaDon.setSelectionBackground(new java.awt.Color(239, 244, 255));
        tblHoaDon.setSelectionForeground(new java.awt.Color(51, 51, 51));
        tblHoaDon.setShowGrid(true);
        tblHoaDon.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(0).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblHoaDon.getColumnModel().getColumn(1).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblHoaDon.getColumnModel().getColumn(2).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(3).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(4).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(5).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(6).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(6).setPreferredWidth(120);
            tblHoaDon.getColumnModel().getColumn(7).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(8).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(8).setPreferredWidth(120);
            tblHoaDon.getColumnModel().getColumn(9).setResizable(false);
            tblHoaDon.getColumnModel().getColumn(9).setPreferredWidth(150);
        }

        panelShadow2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(panelShadow2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String to = kiemTraNgayKetThuc();
        String from = kiemTraNgayBatDau();
        String thang = cmbThang.getSelectedItem().toString();
        String nam = cmbNam.getSelectedItem().toString();
        String quy = cmbQuy.getSelectedItem().toString();
        if(obj.equals(btnLamMoi)){
            dscBatDau.setDate(null);
            dscKetThuc.setDate(null);
            txtTimKiem.setText("");
            cmbThang.setSelectedIndex(0);
            cmbCot.setSelectedIndex(0);
            cmbNam.setSelectedIndex(0);
            cmbQuy.setSelectedIndex(0);
            txtTimKiem.requestFocus();
            xoaDuLieu();
            loadData(pnlPage.getCurrentIndex());
        }
        if(obj.equals(cmbNam)){
            if(cmbNam.getSelectedIndex()!=0){
              if(cmbThang.getSelectedIndex()==0)thang = "";
              if(cmbQuy.getSelectedIndex()==0)quy ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon);
            }else{
              nam = "";
              if(cmbThang.getSelectedIndex()==0)thang = "";
              if(cmbQuy.getSelectedIndex()==0)quy ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon);
            }
        }
        if(obj.equals(cmbQuy)){
            if(cmbQuy.getSelectedIndex()!=0){
              if(cmbThang.getSelectedIndex()==0)thang = "";
              if(cmbNam.getSelectedIndex()==0)nam ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon);
            }else{
              quy = "";
              if(cmbThang.getSelectedIndex()==0)thang = "";
              if(cmbNam.getSelectedIndex()==0)nam ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon); 
            }
        }
        if(obj.equals(cmbThang)){
            if(cmbThang.getSelectedIndex()!=0){
              if(cmbQuy.getSelectedIndex()==0)quy = "";
              if(cmbNam.getSelectedIndex()==0)nam ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon);
            }else{
              thang = "";
              if(cmbQuy.getSelectedIndex()==0)quy = "";
              if(cmbNam.getSelectedIndex()==0)nam ="";
              dsHoaDon = hoaDon_Dao.locHoaDonByThang_Quy_Nam(from, to, thang, quy, nam, pnlPage.getCurrentIndex());
              xoaDuLieu();
              loadPage(hoaDon_Dao.getSoLuongHoaDonByAll(from, to, thang, quy, nam));
              taiLaiDuLieu(dsHoaDon); 
            }
        }
        if(obj.equals(cmbCot)){
            dscBatDau.setDate(null);
            dscKetThuc.setDate(null);
            cmbThang.setSelectedIndex(0);
            cmbNam.setSelectedIndex(0);
            cmbQuy.setSelectedIndex(0);
            xoaDuLieu();
            loadPage(hoaDon_Dao.getSoLuongHoaDon());
            taiLaiDuLieu(dsHoaDon);
            if(cmbCot.getSelectedIndex()==0){
                    txtTimKiem.setHint("Nhập thông tin tìm kiếm theo tùy chọn của bạn.");
                }else if(cmbCot.getSelectedItem().toString().equals("Mã hóa đơn")){
                    txtTimKiem.setHint("Nhập mã hóa đơn muốn tìm.");
                }else if(cmbCot.getSelectedItem().toString().equals("Khách hàng")){
                    txtTimKiem.setHint("Nhập tên khách hàng muốn tìm.");
                }else{
                    txtTimKiem.setHint("Nhập tên phòng hát muốn tìm.");
                }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private gui.swing.panel.PanelShadow panelShadow2;
    private javax.swing.JPanel pnlBottom_Page;
    private gui.swing.panel.PanelShadow pnlForm;
    private gui.swing.table2.PanelPage pnlPage;
    private gui.swing.table2.MyTableFlatlaf tblHoaDon;
    // End of variables declaration//GEN-END:variables
}
