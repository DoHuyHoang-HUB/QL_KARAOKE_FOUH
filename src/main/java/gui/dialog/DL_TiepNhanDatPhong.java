package gui.dialog;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.MatHang_DAO;
import dao.Phong_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.MatHang;
import entity.Phong;
import entity.NhanVien;
import entity.TrangThaiPhong;
import gui.swing.event.EventAdd;
import gui.swing.event.EventMinus;
import gui.swing.image.WindowIcon;
import gui.swing.model.ModelAdd;
import gui.swing.table2.SpinnerEditor;
import gui.swing.textfield.PanelSearch;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import service.ChiTietHoaDonService;
import service.HoaDonService;
import service.KhachHangService;
import service.MatHangService;
import service.PhieuDatPhongService;
import service.PhongService;

public class DL_TiepNhanDatPhong extends javax.swing.JDialog {

    private final MatHangService matHangService;
    private final HoaDonService hoaDonService;
    private final PhongService phongService;
    private PhieuDatPhongService phieuDatPhongService;
    private final KhachHangService khachHangService;
    private final ChiTietHoaDonService chiTietHoaDonService;
    private final HoaDon hoaDon;
    private final DecimalFormat df = new DecimalFormat("#,##0");
    private PanelSearch search;
    private JPopupMenu menu;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DL_TiepNhanDatPhong(Phong phong, NhanVien nv) {
        this.hoaDonService = new HoaDon_DAO();
        this.matHangService = new MatHang_DAO();
        this.khachHangService = new KhachHang_DAO();
        this.phongService = new Phong_DAO();
        this.chiTietHoaDonService = new ChiTietHoaDon_DAO();
        this.hoaDon = new HoaDon("HD0000012", phong, nv);
        WindowIcon.addWindowIcon(this);
        initComponents();
        setModal(true);
        setResizable(false);
        setTitle("Tiếp nhận đặt phòng");
        buildDisplay();
    }

    private void buildDisplay() {
        createTableMatHang();
        createTableCTHoaDon();
        createTxtKhachHang();
        createDateChooseThoiGianBatDau();
        createDateChooseThoiGianKetThuc();
        loadDataForm();
    }

    private void createTableMatHang() {
        loadDataTableMatHang();
    }

    private void createTableCTHoaDon() {
        tableCTHoaDon.getColumnModel().getColumn(2).setCellEditor(new SpinnerEditor(200));
        loadDataTableCTHoaDon();
    }

    private void createDateChooseThoiGianBatDau() {
        thoiGianBatDau.getDatePicker().getSettings().setAllowEmptyDates(false);
        thoiGianBatDau.getTimePicker().getSettings().setAllowEmptyTimes(false);
        thoiGianBatDau.getTimePicker().getSettings().setDisplayToggleTimeMenuButton(false);
        thoiGianBatDau.getTimePicker().getSettings().setDisplaySpinnerButtons(true);
        thoiGianBatDau.getTimePicker().getSettings().setColor(TimePickerSettings.TimeArea.TextFieldBackgroundDisabled, Color.WHITE);
        thoiGianBatDau.getTimePicker().getSettings().setColor(TimePickerSettings.TimeArea.TimePickerTextDisabled, Color.BLACK);
        thoiGianBatDau.getTimePicker().setEnabled(false);
        thoiGianBatDau.getTimePicker().getSettings().setFormatForDisplayTime("HH:mm");
        thoiGianBatDau.getDatePicker().setEnabled(false);
        new Thread(() -> {
            while (true) {
                thoiGianBatDau.getTimePicker().setTime(LocalTime.now());
            }
        }).start();
        thoiGianBatDau.getDatePicker().getSettings().setLocale(new Locale("vi"));
        thoiGianBatDau.getDatePicker().getSettings().setFormatForDatesCommonEra("yyyy/MM/dd");
        thoiGianBatDau.getDatePicker().getSettings().setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisabled, Color.WHITE);
        thoiGianBatDau.getDatePicker().getSettings().setColor(DatePickerSettings.DateArea.DatePickerTextDisabled, Color.BLACK);
        thoiGianBatDau.getDatePicker().getSettings().setFormatForDatesBeforeCommonEra("uuuu/MM/dd");
        thoiGianBatDau.addDateTimeChangeListener(new DateTimeChangeListener() {
            @Override
            public void dateOrTimeChanged(DateTimeChangeEvent event) {
                thoiGianDateOrTimeChanged(event);
            }
        });
    }

    private void createDateChooseThoiGianKetThuc() {
        thoiGianKetThuc.getDatePicker().getSettings().setAllowEmptyDates(false);
        thoiGianKetThuc.getTimePicker().getSettings().setAllowEmptyTimes(false);
        thoiGianKetThuc.getDatePicker().getSettings().setAllowEmptyDates(false);
        thoiGianKetThuc.getTimePicker().getSettings().setAllowEmptyTimes(false);
        thoiGianKetThuc.getTimePicker().setTimeToNow();
        thoiGianKetThuc.getTimePicker().getSettings().setVetoPolicy((LocalTime time) -> PickerUtilities.isLocalTimeInRange(time,
                LocalTime.now(),
                LocalTime.MAX, true));
        thoiGianKetThuc.getTimePicker().getSettings().setDisplaySpinnerButtons(true);
        thoiGianKetThuc.getTimePicker().getSettings().generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FifteenMinutes, null, null);
        thoiGianKetThuc.getTimePicker().getSettings().use24HourClockFormat();
        thoiGianKetThuc.getTimePicker().getSettings().setFormatForDisplayTime("HH:mm");
        thoiGianKetThuc.getTimePicker().getSettings().setFormatForMenuTimes("HH:mm");
        thoiGianKetThuc.getDatePicker().getSettings().setAllowKeyboardEditing(false);
        LocalDate today = LocalDate.now();
        thoiGianKetThuc.getDatePicker().getSettings().setDateRangeLimits(today, LocalDate.MAX);
        thoiGianKetThuc.getDatePicker().getSettings().setColor(DatePickerSettings.DateArea.CalendarBackgroundVetoedDates, Color.LIGHT_GRAY);
        thoiGianKetThuc.getDatePicker().getSettings().setLocale(new Locale("vi"));
        thoiGianKetThuc.getDatePicker().getSettings().setFormatForDatesCommonEra("yyyy/MM/dd");
        thoiGianKetThuc.getDatePicker().getSettings().setFormatForDatesBeforeCommonEra("uuuu/MM/dd");
        thoiGianKetThuc.addDateTimeChangeListener(new DateTimeChangeListener() {
            @Override
            public void dateOrTimeChanged(DateTimeChangeEvent event) {
                thoiGianDateOrTimeChanged(event);
            }
        });
    }

    private void loadDataForm() {
        if (hoaDon.getPhong() != null | hoaDon.getNhanVien() != null) {
            txtMaHoaDon.setText(hoaDon.getMaHoaDon());
            txtTenPhong.setText(hoaDon.getPhong().getTenPhong());
            txtLoaiPhong.setText(hoaDon.getPhong().getLoaiPhong().getTenLoaiPhong());
            lblNhanVien.setText("Nhân viên: " + hoaDon.getNhanVien().getTenNhanVien());
            lblRole.setText(hoaDon.getNhanVien().getLoaiNhanVien().getTenLoaiNV());
            txtTongTienMatHang.setText(df.format(hoaDon.getTongTienMatHang()));
        }
    }

    private void loadDataTableMatHang() {
        List<MatHang> dsMatHang = matHangService.getDsMatHang();
        if (dsMatHang != null) {
            EventAdd event = (Object obj) -> {
                MatHang matHang = (MatHang) obj;
                try {
                    matHang.setsLTonKho(matHang.getsLTonKho() - 1);
                    hoaDon.themCT_HoaDon(matHang, 1, 0);
                    loadDataTableCTHoaDon();
                    ((DefaultTableModel) tableMatHang.getModel()).setValueAt(matHang.getsLTonKho(), tableMatHang.getSelectedRow(), 1);
                    txtTongTienMatHang.setText(df.format(hoaDon.getTongTienMatHang()));
                    txtTienPhongDuKien.setText(df.format(hoaDon.getDonGiaPhong()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Mặt hàng này đã hết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            };

            dsMatHang.forEach(matHang -> {
                ((DefaultTableModel) tableMatHang.getModel()).addRow(matHang.convertToRowTableInGDTiepNhanDatPhong(event));
            });
        }
    }

    private void loadDataTableCTHoaDon() {
        ((DefaultTableModel) tableCTHoaDon.getModel()).setRowCount(0);
        List<ChiTietHoaDon> dsChiTietHoaDon = hoaDon.getDsChiTietHoaDon();
        EventMinus event = () -> {
            try {
                int row = tableCTHoaDon.getSelectedRow();
                ChiTietHoaDon chiTietHoaDon = hoaDon.getDsChiTietHoaDon().get(row);
                MatHang matHang = chiTietHoaDon.getMatHang();
                matHang.setsLTonKho(matHang.getsLTonKho() + chiTietHoaDon.getSoLuong());
                hoaDon.getDsChiTietHoaDon().remove(chiTietHoaDon);
                loadDataTableCTHoaDon();
                ((DefaultTableModel) tableMatHang.getModel()).setValueAt(matHang.getsLTonKho(), tableMatHang.getSelectedRow(), 1);
                txtTongTienMatHang.setText(df.format(hoaDon.getTongTienMatHang()));
                txtTienPhongDuKien.setText(df.format(hoaDon.getDonGiaPhong()));
            } catch (Exception ex) {
                Logger.getLogger(DL_TiepNhanDatPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        dsChiTietHoaDon.forEach(chiTietHoaDon -> {
            ((DefaultTableModel) tableCTHoaDon.getModel()).addRow(chiTietHoaDon.convertToRowTableInTiepNhanHoaDon(event));
        });
    }

    private void createTxtKhachHang() {
        menu = new JPopupMenu();
        search = new PanelSearch("tenKhachHang");
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick((Object obj) -> {
            KhachHang khachHang = (KhachHang) obj;
            hoaDon.setKhachHang(khachHang);
            menu.setVisible(false);
            txtTenKhachHang.setText(khachHang.getTenKhachHang());
            txtSdt.setText(khachHang.getSoDienThoai());
            txtCCCD.setText(khachHang.getCanCuocCD());
        });
    }

    private void searchKhachHang(JTextField textField) {
        try {
            String text = textField.getText().trim().toLowerCase();
            search.setData(khachHangService.getDsKhachHangLimit(text));
            if (search.getItemSize() > 0) {
                menu.show(textField, 0, textField.getHeight());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 30) + 2);
            } else {
                menu.setVisible(false);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(DL_TiepNhanDatPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDataTTKH() {
        KhachHang khachHang = (KhachHang) search.getSelectedRow();
        hoaDon.setKhachHang(khachHang);
        txtTenKhachHang.setText(khachHang.getTenKhachHang());
        txtCCCD.setText(khachHang.getCanCuocCD());
        txtSdt.setText(khachHang.getSoDienThoai());
        menu.setVisible(false);
    }

    private void thoiGianDateOrTimeChanged(DateTimeChangeEvent event) {
        Object obj = event.getSource();
        if (obj.equals(thoiGianKetThuc)) {
            try {
                Date date;
                date = sdf.parse(event.getNewDateTimePermissive().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                hoaDon.setThoiGianKetThuc(date);
                txtTienPhongDuKien.setText(df.format(hoaDon.getDonGiaPhong()));
            } catch (ParseException ex) {
                Logger.getLogger(DL_TiepNhanDatPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Date date;
                date = sdf.parse(event.getNewDateTimePermissive().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                hoaDon.setThoiGianBatDau(date);
                txtTongTienMatHang.setText(df.format(hoaDon.getDonGiaPhong()));
            } catch (ParseException ex) {
                Logger.getLogger(DL_TiepNhanDatPhong.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateTimePicker1 = new com.github.lgooddatepicker.components.DateTimePicker();
        bg = new javax.swing.JPanel();
        pnlMain = new javax.swing.JPanel();
        pnlBottomBar = new javax.swing.JPanel();
        btnGiaoPhong = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();
        lblNhanVien = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnlMatHang = new javax.swing.JPanel();
        txtSearch = new gui.swing.textfield.MyTextField();
        cbLoaiDichVu = new javax.swing.JComboBox<>();
        lblLoaiDichVu = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMatHang = new gui.swing.table2.MyTableFlatlaf();
        pnlCenter = new javax.swing.JPanel();
        lblTenPhong = new javax.swing.JLabel();
        txtTenPhong = new javax.swing.JTextField();
        lblLoaiPhong = new javax.swing.JLabel();
        txtLoaiPhong = new javax.swing.JTextField();
        pnlTGThuePhong = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        thoiGianBatDau = new com.github.lgooddatepicker.components.DateTimePicker();
        thoiGianKetThuc = new com.github.lgooddatepicker.components.DateTimePicker();
        txtTienPhongDuKien = new gui.swing.textfield.MyTextFieldPerUnit();
        lblTienPhongDuKien = new javax.swing.JLabel();
        pnlDatTruoc = new javax.swing.JPanel();
        lblDaCoc = new javax.swing.JLabel();
        txtDaCoc = new gui.swing.textfield.MyTextFieldPerUnit();
        btnExpand = new javax.swing.JToggleButton();
        pnlTTKH = new javax.swing.JPanel();
        lblTenKhachHang = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        lblCCCD = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        lblSdt = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        pnlTTHD = new javax.swing.JPanel();
        txtMaHoaDon = new javax.swing.JTextField();
        lblMaHoaDon = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        lblGiamGia = new javax.swing.JLabel();
        txtChietKhau = new javax.swing.JTextField();
        lblChietKhau = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCTHoaDon = new gui.swing.table2.MyTableFlatlaf();
        txtTongTienMatHang = new gui.swing.textfield.MyTextFieldPerUnit();
        pnlExpand = new javax.swing.JPanel();
        spPhieuDatPhong = new javax.swing.JScrollPane();
        tablePhieuDatPhong = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TIếp nhận thuê phòng");

        bg.setLayout(new java.awt.BorderLayout());

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(new java.awt.BorderLayout());

        pnlBottomBar.setBackground(new java.awt.Color(255, 255, 255));
        pnlBottomBar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        btnGiaoPhong.setText("Giao phòng");
        btnGiaoPhong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGiaoPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiaoPhongActionPerformed(evt);
            }
        });

        btnHuy.setText("Hủy");
        btnHuy.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        lblNhanVien.setText("Nhân viên: Đỗ Huy Hoàng");
        lblNhanVien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblRole.setText("Quản lý");
        lblRole.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRole.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout pnlBottomBarLayout = new javax.swing.GroupLayout(pnlBottomBar);
        pnlBottomBar.setLayout(pnlBottomBarLayout);
        pnlBottomBarLayout.setHorizontalGroup(
            pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBottomBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNhanVien)
                    .addComponent(lblRole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 612, Short.MAX_VALUE)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGiaoPhong)
                .addGap(22, 22, 22))
        );
        pnlBottomBarLayout.setVerticalGroup(
            pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBottomBarLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGiaoPhong)
                    .addComponent(btnHuy))
                .addGap(12, 12, 12))
            .addGroup(pnlBottomBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRole)
                .addGap(5, 5, 5))
        );

        pnlMain.add(pnlBottomBar, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));

        pnlMatHang.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)), "Cập nhật mặt hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnlMatHang.setOpaque(false);

        txtSearch.setBackgroundColor(new java.awt.Color(255, 255, 255));
        txtSearch.setBorderLine(true);
        txtSearch.setHint("Tìm kiếm...");
        txtSearch.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search_25px.png"))); // NOI18N

        cbLoaiDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đồ ăn", "Đồ uống" }));

        lblLoaiDichVu.setText("Loại dịch vụ");
        lblLoaiDichVu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tableMatHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên mặt hàng", "Tồn kho", "Giá bán", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableMatHang.setFillsViewportHeight(true);
        tableMatHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableMatHang.setRowHeight(30);
        tableMatHang.setShowGrid(true);
        tableMatHang.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableMatHang);
        if (tableMatHang.getColumnModel().getColumnCount() > 0) {
            tableMatHang.getColumnModel().getColumn(0).setPreferredWidth(200);
            tableMatHang.getColumnModel().getColumn(1).setResizable(false);
            tableMatHang.getColumnModel().getColumn(1).setPreferredWidth(60);
            tableMatHang.getColumnModel().getColumn(2).setResizable(false);
            tableMatHang.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableMatHang.getColumnModel().getColumn(3).setResizable(false);
            tableMatHang.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        javax.swing.GroupLayout pnlMatHangLayout = new javax.swing.GroupLayout(pnlMatHang);
        pnlMatHang.setLayout(pnlMatHangLayout);
        pnlMatHangLayout.setHorizontalGroup(
            pnlMatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
            .addGroup(pnlMatHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMatHangLayout.createSequentialGroup()
                        .addComponent(lblLoaiDichVu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbLoaiDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMatHangLayout.setVerticalGroup(
            pnlMatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMatHangLayout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbLoaiDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoaiDichVu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(pnlMatHang);

        pnlCenter.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(204, 204, 204)));
        pnlCenter.setOpaque(false);

        lblTenPhong.setText("Tên phòng");
        lblTenPhong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtTenPhong.setText("Phòng 001");
        txtTenPhong.setEnabled(false);

        lblLoaiPhong.setText("Loại phòng");
        lblLoaiPhong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtLoaiPhong.setText("Phòng thường");
        txtLoaiPhong.setEnabled(false);

        pnlTGThuePhong.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Thời gian thuê phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnlTGThuePhong.setOpaque(false);

        jLabel10.setText("Từ");
        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_advance_20px_3.png"))); // NOI18N

        txtTienPhongDuKien.setEnabled(false);
        txtTienPhongDuKien.setUnit("VND");

        lblTienPhongDuKien.setText("Tiền phòng dự kiến");
        lblTienPhongDuKien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlTGThuePhongLayout = new javax.swing.GroupLayout(pnlTGThuePhong);
        pnlTGThuePhong.setLayout(pnlTGThuePhongLayout);
        pnlTGThuePhongLayout.setHorizontalGroup(
            pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTGThuePhongLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlTGThuePhongLayout.createSequentialGroup()
                        .addComponent(lblTienPhongDuKien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTienPhongDuKien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlTGThuePhongLayout.createSequentialGroup()
                        .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlTGThuePhongLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(thoiGianBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(thoiGianKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlTGThuePhongLayout.setVerticalGroup(
            pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTGThuePhongLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(thoiGianBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(thoiGianKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTGThuePhongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienPhongDuKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienPhongDuKien))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDatTruoc.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Đặt trước", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnlDatTruoc.setOpaque(false);

        lblDaCoc.setText("Đã cọc");
        lblDaCoc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtDaCoc.setEnabled(false);
        txtDaCoc.setUnit("VND");

        javax.swing.GroupLayout pnlDatTruocLayout = new javax.swing.GroupLayout(pnlDatTruoc);
        pnlDatTruoc.setLayout(pnlDatTruocLayout);
        pnlDatTruocLayout.setHorizontalGroup(
            pnlDatTruocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatTruocLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDaCoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDaCoc, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlDatTruocLayout.setVerticalGroup(
            pnlDatTruocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatTruocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatTruocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDaCoc)
                    .addComponent(txtDaCoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnExpand.setText("Phiếu đặt phòng");
        btnExpand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnExpandItemStateChanged(evt);
            }
        });

        pnlTTKH.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Thông tin khách hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnlTTKH.setOpaque(false);

        lblTenKhachHang.setText("Tên khách hàng");
        lblTenKhachHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtTenKhachHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTenKhachHang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTenKhachHangFocusGained(evt);
            }
        });
        txtTenKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTenKhachHangKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTenKhachHangKeyReleased(evt);
            }
        });

        lblCCCD.setText("CCCD");
        lblCCCD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtCCCD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCCCD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCCCDFocusGained(evt);
            }
        });
        txtCCCD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCCCDKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCCCDKeyReleased(evt);
            }
        });

        lblSdt.setText("Số điện thoại");
        lblSdt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtSdt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSdt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSdtFocusGained(evt);
            }
        });
        txtSdt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSdtKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSdtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlTTKHLayout = new javax.swing.GroupLayout(pnlTTKH);
        pnlTTKH.setLayout(pnlTTKHLayout);
        pnlTTKHLayout.setHorizontalGroup(
            pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTKHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTenKhachHang)
                    .addComponent(lblCCCD)
                    .addComponent(lblSdt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlTTKHLayout.setVerticalGroup(
            pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTKHLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTenKhachHang))
                .addGap(18, 18, 18)
                .addGroup(pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCCCD))
                .addGap(18, 18, 18)
                .addGroup(pnlTTKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSdt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTTHD.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Thông tin hóa đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        pnlTTHD.setOpaque(false);

        txtMaHoaDon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMaHoaDon.setEnabled(false);

        lblMaHoaDon.setText("Mã hóa đơn");
        lblMaHoaDon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtGiamGia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblGiamGia.setText("Giảm giá");
        lblGiamGia.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtChietKhau.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblChietKhau.setText("Chiết khấu");
        lblChietKhau.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout pnlTTHDLayout = new javax.swing.GroupLayout(pnlTTHD);
        pnlTTHD.setLayout(pnlTTHDLayout);
        pnlTTHDLayout.setHorizontalGroup(
            pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTHDLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGiamGia)
                    .addComponent(lblMaHoaDon)
                    .addComponent(lblChietKhau))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(txtGiamGia)
                    .addComponent(txtChietKhau))
                .addContainerGap())
        );
        pnlTTHDLayout.setVerticalGroup(
            pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTTHDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaHoaDon)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiamGia)
                    .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlTTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChietKhau)
                    .addComponent(txtChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Tổng tiền mặt hàng");
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tableCTHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tên mặt hàng", "Số lượng", "Đơn giá", "Thành tiền", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCTHoaDon.setFillsViewportHeight(true);
        tableCTHoaDon.setRowHeight(30);
        tableCTHoaDon.setShowGrid(true);
        tableCTHoaDon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableCTHoaDonKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tableCTHoaDon);
        if (tableCTHoaDon.getColumnModel().getColumnCount() > 0) {
            tableCTHoaDon.getColumnModel().getColumn(0).setResizable(false);
            tableCTHoaDon.getColumnModel().getColumn(0).setPreferredWidth(60);
            tableCTHoaDon.getColumnModel().getColumn(1).setPreferredWidth(200);
            tableCTHoaDon.getColumnModel().getColumn(2).setResizable(false);
            tableCTHoaDon.getColumnModel().getColumn(2).setPreferredWidth(60);
            tableCTHoaDon.getColumnModel().getColumn(3).setResizable(false);
            tableCTHoaDon.getColumnModel().getColumn(3).setPreferredWidth(100);
            tableCTHoaDon.getColumnModel().getColumn(4).setResizable(false);
            tableCTHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
            tableCTHoaDon.getColumnModel().getColumn(5).setResizable(false);
        }

        txtTongTienMatHang.setEnabled(false);
        txtTongTienMatHang.setUnit("VND");

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCenterLayout.createSequentialGroup()
                        .addComponent(lblTenPhong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenPhong)
                        .addGap(16, 16, 16)
                        .addComponent(lblLoaiPhong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnExpand))
                    .addGroup(pnlCenterLayout.createSequentialGroup()
                        .addGap(0, 332, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTienMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlTGThuePhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTTKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlTTHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDatTruoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenPhong)
                    .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLoaiPhong)
                    .addComponent(txtLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExpand))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTongTienMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTTKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlTTHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTGThuePhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDatTruoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(pnlCenter);

        pnlMain.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        bg.add(pnlMain, java.awt.BorderLayout.CENTER);

        pnlExpand.setBackground(new java.awt.Color(255, 255, 255));
        pnlExpand.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)), "Phiếu đặt phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tablePhieuDatPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu", "Khách hàng", "Giờ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePhieuDatPhong.setFillsViewportHeight(true);
        spPhieuDatPhong.setViewportView(tablePhieuDatPhong);
        if (tablePhieuDatPhong.getColumnModel().getColumnCount() > 0) {
            tablePhieuDatPhong.getColumnModel().getColumn(0).setResizable(false);
            tablePhieuDatPhong.getColumnModel().getColumn(1).setResizable(false);
            tablePhieuDatPhong.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout pnlExpandLayout = new javax.swing.GroupLayout(pnlExpand);
        pnlExpand.setLayout(pnlExpandLayout);
        pnlExpandLayout.setHorizontalGroup(
            pnlExpandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExpandLayout.createSequentialGroup()
                .addComponent(spPhieuDatPhong, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnlExpandLayout.setVerticalGroup(
            pnlExpandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlExpandLayout.createSequentialGroup()
                .addComponent(spPhieuDatPhong, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
                .addContainerGap())
        );

        bg.add(pnlExpand, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1026, 637));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnExpandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnExpandItemStateChanged
        int state = evt.getStateChange();
        if (state == ItemEvent.SELECTED) {
            setSize(new java.awt.Dimension(getPreferredSize()));
            setLocationRelativeTo(null);
        } else {
            setSize(new java.awt.Dimension(1026, 637));
            setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_btnExpandItemStateChanged

    private void tableCTHoaDonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCTHoaDonKeyReleased
        int row = tableCTHoaDon.getSelectedRow();
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            int sl = (int) ((DefaultTableModel) tableCTHoaDon.getModel()).getValueAt(row, 2);
            MatHang matHang = hoaDon.getDsChiTietHoaDon().get(row).getMatHang();
            try {
                matHang.setsLTonKho(matHangService.getMatHang(matHang.getMaMatHang()).getsLTonKho() - sl);
                hoaDon.getDsChiTietHoaDon().get(tableCTHoaDon.getSelectedRow()).setSoLuong(0);
                hoaDon.themCT_HoaDon(matHang, sl, 0);
                loadDataTableCTHoaDon();
                for (int i = 0; i < tableMatHang.getRowCount(); i++) {
                    ModelAdd data = (ModelAdd) ((DefaultTableModel) tableMatHang.getModel()).getValueAt(i, 3);
                    MatHang mh = (MatHang) data.getObj();
                    if (mh.equals(matHang)) {
                        ((DefaultTableModel) tableMatHang.getModel()).setValueAt(matHang.getsLTonKho(), i, 1);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Mặt hàng không đủ số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                loadDataTableCTHoaDon();
            }
        } else {

        }
    }//GEN-LAST:event_tableCTHoaDonKeyReleased

    private void txtTenKhachHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenKhachHangKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            searchKhachHang(txtTenKhachHang);
        }
    }//GEN-LAST:event_txtTenKhachHangKeyReleased

    private void txtTenKhachHangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenKhachHangKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loadDataTTKH();
        }
    }//GEN-LAST:event_txtTenKhachHangKeyPressed

    private void txtCCCDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCCDKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            searchKhachHang(txtCCCD);
        }
    }//GEN-LAST:event_txtCCCDKeyReleased

    private void txtCCCDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCCDKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loadDataTTKH();
        }
    }//GEN-LAST:event_txtCCCDKeyPressed

    private void txtTenKhachHangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTenKhachHangFocusGained
        search.setColumnName("tenKhachHang");
        if (search.getItemSize() > 0) {
            menu.show(txtTenKhachHang, 0, txtTenKhachHang.getHeight());
            search.clearSelected();
            searchKhachHang(txtTenKhachHang);

        }
    }//GEN-LAST:event_txtTenKhachHangFocusGained

    private void txtCCCDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCCCDFocusGained
        search.setColumnName("canCuocCD");
        if (search.getItemSize() > 0) {
            menu.show(txtCCCD, 0, txtCCCD.getHeight());
            search.clearSelected();
            searchKhachHang(txtCCCD);
        }
    }//GEN-LAST:event_txtCCCDFocusGained

    private void txtSdtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSdtFocusGained
        search.setColumnName("soDienThoai");
        if (search.getItemSize() > 0) {
            menu.show(txtSdt, 0, txtSdt.getHeight());
            search.clearSelected();
            searchKhachHang(txtSdt);
        }
    }//GEN-LAST:event_txtSdtFocusGained

    private void txtSdtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSdtKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            searchKhachHang(txtSdt);
        }
    }//GEN-LAST:event_txtSdtKeyReleased

    private void txtSdtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSdtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loadDataTTKH();
        }
    }//GEN-LAST:event_txtSdtKeyPressed

    private void btnGiaoPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiaoPhongActionPerformed
        hoaDon.getPhong().setTrangThai(TrangThaiPhong.DANG_HAT);
        hoaDonService.addHoaDon(hoaDon);
        hoaDon.getDsChiTietHoaDon().forEach(chiTietHoaDon -> {
            chiTietHoaDonService.addChiTietHoaDon(chiTietHoaDon);
        });
        phongService.updatePhong(hoaDon.getPhong());
        dispose();
    }//GEN-LAST:event_btnGiaoPhongActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JToggleButton btnExpand;
    private javax.swing.JButton btnGiaoPhong;
    private javax.swing.JButton btnHuy;
    private javax.swing.JComboBox<String> cbLoaiDichVu;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblCCCD;
    private javax.swing.JLabel lblChietKhau;
    private javax.swing.JLabel lblDaCoc;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblLoaiDichVu;
    private javax.swing.JLabel lblLoaiPhong;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSdt;
    private javax.swing.JLabel lblTenKhachHang;
    private javax.swing.JLabel lblTenPhong;
    private javax.swing.JLabel lblTienPhongDuKien;
    private javax.swing.JPanel pnlBottomBar;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDatTruoc;
    private javax.swing.JPanel pnlExpand;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMatHang;
    private javax.swing.JPanel pnlTGThuePhong;
    private javax.swing.JPanel pnlTTHD;
    private javax.swing.JPanel pnlTTKH;
    private javax.swing.JScrollPane spPhieuDatPhong;
    private gui.swing.table2.MyTableFlatlaf tableCTHoaDon;
    private gui.swing.table2.MyTableFlatlaf tableMatHang;
    private javax.swing.JTable tablePhieuDatPhong;
    private com.github.lgooddatepicker.components.DateTimePicker thoiGianBatDau;
    private com.github.lgooddatepicker.components.DateTimePicker thoiGianKetThuc;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtChietKhau;
    private gui.swing.textfield.MyTextFieldPerUnit txtDaCoc;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtLoaiPhong;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtSdt;
    private gui.swing.textfield.MyTextField txtSearch;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTenPhong;
    private gui.swing.textfield.MyTextFieldPerUnit txtTienPhongDuKien;
    private gui.swing.textfield.MyTextFieldPerUnit txtTongTienMatHang;
    // End of variables declaration//GEN-END:variables
}
