package com.example.quanlysach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysach.R;
import com.example.quanlysach.model.Book;

import java.util.ArrayList;

/**
 * BookAdapter - RecyclerView.Adapter hiển thị danh sách sách.
 * Dùng ViewHolder pattern để tái sử dụng view, tránh lãng phí bộ nhớ.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private final ArrayList<Book> danhSachSach; // Danh sách sách cần hiển thị

    // Interface callback khi người dùng bấm vào một sách
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public BookAdapter(Context context, ArrayList<Book> danhSachSach) {
        this.context = context;
        this.danhSachSach = danhSachSach;
    }

    // Gán listener từ Activity
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_book.xml cho mỗi dòng sách
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = danhSachSach.get(position);

        // Gán dữ liệu vào các view
        holder.tvTenSach.setText(book.getTenSach());
        holder.tvTacGia.setText(book.getTacGia());
        holder.tvTheLoai.setText(book.getTheLoai());
        holder.tvTinhTrang.setText(book.getTinhTrang());
        holder.ratingBar.setRating(book.getDanhGia());

        // Đổi màu badge tình trạng tùy giá trị
        switch (book.getTinhTrang()) {
            case "Đã đọc":
                holder.tvTinhTrang.setBackgroundResource(R.drawable.badge_green);
                break;
            case "Đang đọc":
                holder.tvTinhTrang.setBackgroundResource(R.drawable.badge_orange);
                break;
            default: // "Chưa đọc"
                holder.tvTinhTrang.setBackgroundResource(R.drawable.badge_gray);
                break;
        }

        // Xử lý click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachSach.size();
    }

    /**
     * ViewHolder lưu tham chiếu các view của mỗi item để tái sử dụng.
     */
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSach, tvTacGia, tvTheLoai, tvTinhTrang;
        RatingBar ratingBar;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSach   = itemView.findViewById(R.id.tvTenSach);
            tvTacGia    = itemView.findViewById(R.id.tvTacGia);
            tvTheLoai   = itemView.findViewById(R.id.tvTheLoai);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang);
            ratingBar   = itemView.findViewById(R.id.ratingBarItem);
        }
    }
}
