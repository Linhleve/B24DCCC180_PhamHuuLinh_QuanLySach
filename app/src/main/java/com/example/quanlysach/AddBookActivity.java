package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysach.model.Book;

/**
 * AddBookActivity - Màn hình thêm sách mới vào thư viện.
 */
public class AddBookActivity extends AppCompatActivity {

    private EditText etTenSach, etTacGia, etGhiChu;
    private Spinner spinnerTheLoai;
    private RadioGroup radioGroupTinhTrang;
    private RadioButton rbChuaDoc, rbDangDoc, rbDaDoc;
    private RatingBar ratingBar;
    private TextView tvSoSao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thiết lập Toolbar với nút Back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm sách mới");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Ánh xạ view
        etTenSach           = findViewById(R.id.etTenSach);
        etTacGia            = findViewById(R.id.etTacGia);
        etGhiChu            = findViewById(R.id.etGhiChu);
        spinnerTheLoai      = findViewById(R.id.spinnerTheLoai);
        radioGroupTinhTrang = findViewById(R.id.radioGroupTinhTrang);
        rbChuaDoc           = findViewById(R.id.rbChuaDoc);
        rbDangDoc           = findViewById(R.id.rbDangDoc);
        rbDaDoc             = findViewById(R.id.rbDaDoc);
        ratingBar           = findViewById(R.id.ratingBar);
        tvSoSao             = findViewById(R.id.tvSoSao);

        // Thiết lập Spinner thể loại từ string-array
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.the_loai_array,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheLoai.setAdapter(spinnerAdapter);

        // Mặc định chọn "Chưa đọc"
        rbChuaDoc.setChecked(true);

        // Cập nhật TextView số sao khi người dùng thay đổi RatingBar
        tvSoSao.setText("0 sao");
        ratingBar.setOnRatingBarChangeListener((rb, rating, fromUser) -> {
            int soSao = (int) rating;
            tvSoSao.setText(soSao + " sao");
        });

        // Nút Lưu sách
        Button btnLuuSach = findViewById(R.id.btnLuuSach);
        btnLuuSach.setOnClickListener(v -> luuSach());
    }

    /**
     * Kiểm tra hợp lệ và trả Book mới về MainActivity.
     */
    private void luuSach() {
        String tenSach = etTenSach.getText().toString().trim();
        String tacGia  = etTacGia.getText().toString().trim();
        String ghiChu  = etGhiChu.getText().toString().trim();

        // Validate: tên sách không được trống
        if (TextUtils.isEmpty(tenSach)) {
            etTenSach.setError("Vui lòng nhập tên sách");
            etTenSach.requestFocus();
            return;
        }

        // Validate: tác giả không được trống
        if (TextUtils.isEmpty(tacGia)) {
            etTacGia.setError("Vui lòng nhập tên tác giả");
            etTacGia.requestFocus();
            return;
        }

        // Lấy thể loại từ Spinner
        String theLoai = spinnerTheLoai.getSelectedItem().toString();

        // Lấy tình trạng từ RadioGroup
        String tinhTrang;
        int selectedId = radioGroupTinhTrang.getCheckedRadioButtonId();
        if (selectedId == R.id.rbDangDoc) {
            tinhTrang = "Đang đọc";
        } else if (selectedId == R.id.rbDaDoc) {
            tinhTrang = "Đã đọc";
        } else {
            tinhTrang = "Chưa đọc";
        }

        // Lấy số sao
        int danhGia = (int) ratingBar.getRating();

        // Tạo đối tượng Book mới
        Book sachMoi = new Book(tenSach, tacGia, theLoai, tinhTrang, danhGia, ghiChu);

        // Trả kết quả về MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("book_moi", sachMoi);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Nút Back trên Toolbar → trả RESULT_CANCELED
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }
}
