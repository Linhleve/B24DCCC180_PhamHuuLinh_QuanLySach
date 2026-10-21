package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysach.adapter.BookAdapter;
import com.example.quanlysach.model.Book;

import java.util.ArrayList;

/**
 * MainActivity - Màn hình chính, hiển thị danh sách sách và thống kê.
 */
public class MainActivity extends AppCompatActivity {

    // Danh sách sách dùng chung (bộ nhớ trong)
    private ArrayList<Book> danhSachSach;
    private BookAdapter bookAdapter;

    // View thống kê
    private TextView tvTongSoSach, tvDaDoc, tvChuaDocXong;

    // Launcher nhận kết quả từ AddBookActivity
    private ActivityResultLauncher<Intent> themSachLauncher;
    // Launcher nhận kết quả từ BookDetailActivity
    private ActivityResultLauncher<Intent> chiTietLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view thống kê
        tvTongSoSach  = findViewById(R.id.tvTongSoSach);
        tvDaDoc       = findViewById(R.id.tvDaDoc);
        tvChuaDocXong = findViewById(R.id.tvChuaDocXong);

        // Khởi tạo danh sách sách mẫu ban đầu
        danhSachSach = taoSachMau();

        // Thiết lập RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSach);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, danhSachSach);
        recyclerView.setAdapter(bookAdapter);

        // Click vào sách → mở BookDetailActivity
        bookAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("book", danhSachSach.get(position)); // Truyền đối tượng Book
            intent.putExtra("position", position);               // Truyền vị trí để cập nhật
            chiTietLauncher.launch(intent);
        });

        // Đăng ký launcher cho AddBookActivity
        themSachLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::xuLyKetQuaThemSach
        );

        // Đăng ký launcher cho BookDetailActivity
        chiTietLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::xuLyKetQuaChiTiet
        );

        // Nút "Thêm sách"
        Button btnThemSach = findViewById(R.id.btnThemSach);
        btnThemSach.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBookActivity.class);
            themSachLauncher.launch(intent);
        });

        // Cập nhật thống kê ban đầu
        capNhatThongKe();
    }

    /**
     * Tạo 3 cuốn sách mẫu ban đầu theo yêu cầu.
     */
    private ArrayList<Book> taoSachMau() {
        ArrayList<Book> ds = new ArrayList<>();
        ds.add(new Book("Lập trình Android cơ bản", "Nguyễn Văn A",
                "Công nghệ", "Đang đọc", 4, ""));
        ds.add(new Book("Đắc nhân tâm", "Dale Carnegie",
                "Kỹ năng sống", "Đã đọc", 5, "Sách hay, đọc lại nhiều lần."));
        ds.add(new Book("Dế Mèn phiêu lưu ký", "Tô Hoài",
                "Văn học", "Chưa đọc", 0, ""));
        return ds;
    }

    /**
     * Xử lý kết quả trả về từ AddBookActivity.
     * RESULT_OK → thêm sách mới vào danh sách.
     */
    private void xuLyKetQuaThemSach(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Book sachMoi = (Book) result.getData().getSerializableExtra("book_moi");
            if (sachMoi != null) {
                danhSachSach.add(sachMoi);
                bookAdapter.notifyItemInserted(danhSachSach.size() - 1);
                capNhatThongKe();
            }
        }
    }

    /**
     * Xử lý kết quả trả về từ BookDetailActivity.
     * "cap_nhat" → cập nhật thông tin sách tại position.
     * "xoa"      → xóa sách tại position.
     */
    private void xuLyKetQuaChiTiet(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            String hanh_dong = result.getData().getStringExtra("hanh_dong");
            int position = result.getData().getIntExtra("position", -1);

            if (position < 0 || position >= danhSachSach.size()) return;

            if ("xoa".equals(hanh_dong)) {
                // Xóa sách khỏi danh sách
                danhSachSach.remove(position);
                bookAdapter.notifyItemRemoved(position);
                bookAdapter.notifyItemRangeChanged(position, danhSachSach.size());
            } else if ("cap_nhat".equals(hanh_dong)) {
                // Cập nhật thông tin sách đã chỉnh sửa
                Book sachCapNhat = (Book) result.getData().getSerializableExtra("book_cap_nhat");
                if (sachCapNhat != null) {
                    danhSachSach.set(position, sachCapNhat);
                    bookAdapter.notifyItemChanged(position);
                }
            }
            capNhatThongKe();
        }
    }

    /**
     * Tính và cập nhật TextView thống kê số sách.
     */
    private void capNhatThongKe() {
        int tong = danhSachSach.size();
        int daDoc = 0;
        int chuaDocXong = 0;

        for (Book book : danhSachSach) {
            if ("Đã đọc".equals(book.getTinhTrang())) {
                daDoc++;
            } else {
                // "Chưa đọc" và "Đang đọc" đều tính là chưa đọc xong
                chuaDocXong++;
            }
        }

        tvTongSoSach.setText("Tổng số sách: " + tong);
        tvDaDoc.setText("Đã đọc: " + daDoc);
        tvChuaDocXong.setText("Chưa đọc xong: " + chuaDocXong);
    }
}
