package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;
import gui.swing.graphics.ShadowType;
import gui.swing.button.Button;
import gui.swing.panel.PanelShadow;
import gui.swing.table2.EventAction;
import gui.swing.table2.ModelAction;
import gui.swing.textfield.MyTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import gui.swing.event.EventSelectedRow;

/**
 *
 * @author Hao
 */
public class GD_KhachHang extends javax.swing.JPanel implements ActionListener, KeyListener{
    List<KhachHang> dsKhachHang = new ArrayList<KhachHang>();
    private KhachHang_DAO khachHang_Dao;
    private MyTextField txtTimKiem;
    private Button btnLamMoi;
    private EventAction eventAction;
    private PanelShadow panelHidden;
    private EventSelectedRow eventSelectedRow;
    /**
     * Creates new form GD_KhachHang
     */
    public GD_KhachHang() {
        initComponents();
        buildGD();
        
    }
    
    private void buildGD(){
        khachHang_Dao = new KhachHang_DAO();

        String fontName = "sansserif";
        int fontStyle = Font.PLAIN;
        int fontSize = 14;
        Color colorBtn = new Color(184, 238, 241);
        
        pnlTop.setLayout(new MigLayout("", "push[center]5[center] 20[center]push", "60[center]10"));
        pnlTop.add(createPanelTitle(), "span,pos 0al 0al 100% n, h 40!");
      
        JLabel lblKhachHang = new JLabel("Nhập tên/ số điện thoại (các số cuối)");
        lblKhachHang.setFont(new Font(fontName, fontStyle, fontSize));
        pnlTop.add(lblKhachHang);
        
        /*Ô nhập thông tin tìm kiếm*/
        txtTimKiem = new MyTextField();
        txtTimKiem.setFont(new Font(fontName, fontStyle, fontSize));
        txtTimKiem.setBorderLine(true);
        txtTimKiem.setBorderRadius(5);
        pnlTop.add(txtTimKiem, "w 40%, h 36!");
        
        btnLamMoi = new Button("Làm mới");
        btnLamMoi.setFont(new Font(fontName, fontStyle, fontSize));
        btnLamMoi.setBackground(colorBtn);
        btnLamMoi.setBorderline(true);
        btnLamMoi.setBorderRadius(5);
        pnlTop.add(btnLamMoi, "w 100!, h 36!");
        
        btnLamMoi.addActionListener(this);
        txtTimKiem.addKeyListener(this);
        
        //xuLySuKien();
        createTable();
        setOpaque(false);
        setPreferredSize(new Dimension(getWidth(), 950));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new gui.swing.panel.PanelShadow();
        pnlBottom = new gui.swing.panel.PanelShadow();
        scrKhachHang = new javax.swing.JScrollPane();
        tblKhachHang = new gui.swing.table2.MyTable();
        lblTitleBang = new javax.swing.JLabel();

        pnlTop.setBackground(new java.awt.Color(255, 255, 255));
        pnlTop.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        pnlTop.setShadowOpacity(0.3F);
        pnlTop.setShadowSize(3);
        pnlTop.setShadowType(gui.swing.graphics.ShadowType.TOP);

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1162, Short.MAX_VALUE)
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
        );

        pnlBottom.setBackground(new java.awt.Color(255, 255, 255));
        pnlBottom.setShadowOpacity(0.3F);
        pnlBottom.setShadowSize(3);
        pnlBottom.setShadowType(gui.swing.graphics.ShadowType.TOP);
        pnlBottom.setLayout(new java.awt.BorderLayout());

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Mã khách hàng", "Tên khách hàng", "Căn cước công dân", "Số điện thoại", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.setAlignmentX(0.0F);
        tblKhachHang.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        scrKhachHang.setViewportView(tblKhachHang);
        if (tblKhachHang.getColumnModel().getColumnCount() > 0) {
            tblKhachHang.getColumnModel().getColumn(0).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(1).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(2).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(3).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(4).setResizable(false);
            tblKhachHang.getColumnModel().getColumn(5).setResizable(false);
        }

        pnlBottom.add(scrKhachHang, java.awt.BorderLayout.CENTER);

        lblTitleBang.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblTitleBang.setForeground(new java.awt.Color(4, 72, 210));
        lblTitleBang.setText("  Danh sách khách hàng");
        lblTitleBang.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblTitleBang.setPreferredSize(new java.awt.Dimension(130, 45));
        lblTitleBang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pnlBottom.add(lblTitleBang, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBottom, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private JPanel createPanelTitle() {
        JPanel pnlTitle = new JPanel();
        pnlTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 0.1f)));
        pnlTitle.setOpaque(false);
        pnlTitle.setLayout(new MigLayout("fill", "", ""));
        JLabel lblTitle = new JLabel();
        lblTitle.setText("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));
        lblTitle.setForeground(new Color(68, 68, 68));
        pnlTitle.add(lblTitle);
        return  pnlTitle;
    }
    
    private void loadData() {
        eventAction = new EventAction() {
            @Override
            public void delete(Object obj) {
                JOptionPane.showMessageDialog(GD_KhachHang.this, "Bạn không thể xóa khách hàng");
//                int row = tblKhachHang.getSelectedRow();
//                KhachHang khachHang = (KhachHang) obj;
//                if(JOptionPane.showConfirmDialog(GD_KhachHang.this, "Bạn có chắc chắn muốn xóa khách hàng " +khachHang.getMaKhachHang(), "Delete", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
//                    String s = khachHang_Dao.xoaKhachHang(khachHang.getMaKhachHang())==true? "Xóa thành công":"Thất bại";
//                    JOptionPane.showMessageDialog(GD_KhachHang.this,s);
//                    dsKhachHang = khachHang_Dao.getDSKhachHang();
//                    xoaDuLieu();
//                    taiLaiDuLieu(dsKhachHang);
//                }
            }

            @Override
            public void update(ModelAction action) {
                int row = tblKhachHang.getSelectedRow();
                KhachHang kh = (KhachHang) action.getObj();
                kh.setSoDienThoai(tblKhachHang.getValueAt(row, 4).toString());
               
                String s=  khachHang_Dao.capNhatKhachHang(kh)==true?"Cập nhật thành công":"Thất bại";
                JOptionPane.showMessageDialog(null, s);
                dsKhachHang = khachHang_Dao.getDSKhachHang();
                xoaDuLieu();
                taiLaiDuLieu(dsKhachHang);
            }
                
        };
                dsKhachHang = khachHang_Dao.getDSKhachHang();
                xoaDuLieu();
                taiLaiDuLieu(dsKhachHang);
                
                
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }
        });
    }
    
    
    public void xoaDuLieu(){
        DefaultTableModel df = (DefaultTableModel) tblKhachHang.getModel();
        df.setRowCount(0);
    }
    
    public void taiLaiDuLieu(List<KhachHang> dsKhachHang){
        dsKhachHang.forEach((khachHang) -> {
             tblKhachHang.addRow(khachHang.convertToRowTable(eventAction));
        });
    }
    
    private void createTable() {
        tblKhachHang.fixTable(scrKhachHang);
        loadData();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitleBang;
    private gui.swing.panel.PanelShadow pnlBottom;
    private gui.swing.panel.PanelShadow pnlTop;
    private javax.swing.JScrollPane scrKhachHang;
    private gui.swing.table2.MyTable tblKhachHang;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj.equals(btnLamMoi)){
           txtTimKiem.setText("");
           dsKhachHang = khachHang_Dao.getDSKhachHang();
            xoaDuLieu();
            taiLaiDuLieu(dsKhachHang);
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        Object obj = arg0.getSource();
        if(obj.equals(txtTimKiem)){
            List<KhachHang> dsKhachHang = khachHang_Dao.layDSKhachHang(txtTimKiem.getText().trim());
            xoaDuLieu();
            taiLaiDuLieu(dsKhachHang);
        }
    }

    
}
