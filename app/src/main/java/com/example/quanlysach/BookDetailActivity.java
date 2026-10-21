package com.example.quanlysach;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysach.model.Book;

/**
 * BookDetailActivity - Xem chi tiết, chỉnh sửa đánh giá, đổi trạng thái, hoặc xóa sách.
 */
public class BookDetailActivity extends AppCompatActivity {

    private Book book;        // Đối tượng sách hiện tại
    private int position;     // Vị trí trong danh sách

    private TextView tvTenSach, tvTacGiaPhanLoai, tvTinhTrang, tvGhiChu;
    private RatingBar ratingBarDetail;
    private Button btnDanhDauDaDoc, btnQuayLai, btnXoaSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thiết lập Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết sách");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Nhận Book và position từ Intent
        book     = (Book) getIntent().getSerializableExtra("book");
        position = getIntent().getIntExtra("position", -1);

        // Ánh xạ view
        tvTenSach          = findViewById(R.id.tvTenSachDetail);
        tvTacGiaPhanLoai   = findViewById(R.id.tvTacGiaPhanLoai);
        tvTinhTrang        = findViewById(R.id.tvTinhTrangDetail);
        tvGhiChu           = findViewById(R.id.tvGhiChuDetail);
        ratingBarDetail    = findViewById(R.id.ratingBarDetail);
        btnDanhDauDaDoc    = findViewById(R.id.btnDanhDauDaDoc);
        btnQuayLai         = findViewById(R.id.btnQuayLai);
        btnXoaSach         = findViewById(R.id.btnXoaSach);

        // Hiển thị thông tin sách
        hienThiThongTinSach();

        // Cập nhật đánh giá ngay khi người dùng thay đổi RatingBar
        ratingBarDetail.setOnRatingBarChangeListener((rb, rating, fromUser) -> {
            if (fromUser) {
                book.setDanhGia((int) rating);
            }
        });

        // Nút "Đánh dấu đã đọc"
        btnDanhDauDaDoc.setOnClickListener(v -> {
            book.setTinhTrang("Đã đọc");
            tvTinhTrang.setText("Đã đọc");
            // Ẩn nút vì sách đã chuyển sang "Đã đọc"
            btnDanhDauDaDoc.setVisibility(View.GONE);
        });

        // Nút "Quay lại" → trả dữ liệu đã cập nhật về MainActivity
        btnQuayLai.setOnClickListener(v -> traKetQuaCapNhat());

        // Nút "Xóa sách" → hỏi xác nhận rồi trả kết quả xóa
        btnXoaSach.setOnClickListener(v -> xacNhanXoaSach());

        // Xử lý nút Back vật lý của hệ thống → cũng trả dữ liệu
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                traKetQuaCapNhat();
            }
        });
    }

    /**
     * Hiển thị tất cả thông tin của sách lên các TextView.
     */
    private void hienThiThongTinSach() {
        tvTenSach.setText(book.getTenSach());
        tvTacGiaPhanLoai.setText(book.getTacGia() + " — " + book.getTheLoai());
        tvTinhTrang.setText(book.getTinhTrang());
        ratingBarDetail.setRating(book.getDanhGia());

        // Hiển thị ghi chú, nếu trống thì hiện "Không có ghi chú"
        String ghiChu = book.getGhiChu();
        if (ghiChu == null || ghiChu.isEmpty()) {
            tvGhiChu.setText("Không có ghi chú");
        } else {
            tvGhiChu.setText(ghiChu);
        }

        // Ẩn nút "Đánh dấu đã đọc" nếu sách đã ở trạng thái "Đã đọc"
        if ("Đã đọc".equals(book.getTinhTrang())) {
            btnDanhDauDaDoc.setVisibility(View.GONE);
        } else {
            btnDanhDauDaDoc.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Trả kết quả "cap_nhat" cùng dữ liệu Book đã chỉnh về MainActivity.
     */
    private void traKetQuaCapNhat() {
        // Đảm bảo đánh giá mới nhất được lưu vào book
        book.setDanhGia((int) ratingBarDetail.getRating());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("hanh_dong", "cap_nhat");
        resultIntent.putExtra("position", position);
        resultIntent.putExtra("book_cap_nhat", book);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Hiển thị AlertDialog xác nhận trước khi xóa sách.
     */
    private void xacNhanXoaSach() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sách")
                .setMessage("Bạn có chắc muốn xóa \"" + book.getTenSach() + "\" khỏi thư viện không?")
                .setPositiveButton("Xóa", (dialog, which) -> traKetQuaXoaSach())
                .setNegativeButton("Hủy", null)
                .show();
    }

    /**
     * Trả kết quả "xoa" cùng position về MainActivity để xóa khỏi danh sách.
     */
    private void traKetQuaXoaSach() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("hanh_dong", "xoa");
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Nút Back trên Toolbar cũng trả dữ liệu
        traKetQuaCapNhat();
        return true;
    }
}
