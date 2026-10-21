package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * LoginActivity - Màn hình đăng nhập, là màn hình đầu tiên của ứng dụng.
 * Tài khoản hard-code: admin / 123456.
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản đăng nhập cố định
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    private EditText etTenDangNhap, etMatKhau;
    private TextView tvThongBaoLoi;
    private Button btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ẩn thanh ActionBar ở màn hình Đăng nhập
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        etTenDangNhap  = findViewById(R.id.etTenDangNhap);
        etMatKhau      = findViewById(R.id.etMatKhau);
        tvThongBaoLoi  = findViewById(R.id.tvThongBaoLoi);
        btnDangNhap    = findViewById(R.id.btnDangNhap);

        // Ẩn thông báo lỗi ban đầu
        tvThongBaoLoi.setVisibility(View.GONE);

        btnDangNhap.setOnClickListener(v -> xuLyDangNhap());
    }

    /**
     * Kiểm tra thông tin đăng nhập và điều hướng sang MainActivity nếu đúng.
     */
    private void xuLyDangNhap() {
        String tenDangNhap = etTenDangNhap.getText().toString().trim();
        String matKhau     = etMatKhau.getText().toString().trim();

        // Kiểm tra bỏ trống
        if (TextUtils.isEmpty(tenDangNhap)) {
            etTenDangNhap.setError("Vui lòng nhập tên đăng nhập");
            etTenDangNhap.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(matKhau)) {
            etMatKhau.setError("Vui lòng nhập mật khẩu");
            etMatKhau.requestFocus();
            return;
        }

        // So sánh với tài khoản hard-code
        if (tenDangNhap.equals(VALID_USERNAME) && matKhau.equals(VALID_PASSWORD)) {
            // Đúng: chuyển sang MainActivity và đóng LoginActivity
            tvThongBaoLoi.setVisibility(View.GONE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Ngăn Back quay lại màn hình Login
        } else {
            // Sai: hiện thông báo lỗi
            tvThongBaoLoi.setText(R.string.loi_dang_nhap);
            tvThongBaoLoi.setVisibility(View.VISIBLE);
        }
    }
}
