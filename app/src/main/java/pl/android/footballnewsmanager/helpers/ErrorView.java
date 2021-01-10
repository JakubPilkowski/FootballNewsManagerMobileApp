package pl.android.footballnewsmanager.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.footballnewsmanager.R;

public class ErrorView extends LinearLayout {

    private Context context;
    private View errorImage;
    private View errorImageDefault;
    private TextView errorText;
    private TextView errorButton;

    public interface OnTryAgainListener {
       void onClick();
    }

    public void setOnTryAgainListener(OnTryAgainListener onTryAgainListener) {
        errorButton.setOnClickListener(v -> onTryAgainListener.onClick());
    }

    private void initControl()
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutRes(), this);
        errorImage = findViewById(R.id.error_image);
        errorText = findViewById(R.id.error_text);
        errorButton = findViewById(R.id.error_button);
        errorImageDefault = findViewById(R.id.error_image_default);
    }

    public void setStatus(int status) {
        String message;
        Drawable drawable;
        switch (status) {
            case 408:
                message = getResources().getString(R.string.connection_timeout);
                drawable = context.getDrawable(R.drawable.ic_error_timeout);
                errorImageDefault.setVisibility(GONE);
                errorImage.setBackground(drawable);
                errorImage.setVisibility(VISIBLE);
                break;
            case 598:
                message = getResources().getString(R.string.no_network);
                drawable = context.getDrawable(R.drawable.ic_error_no_network);
                errorImageDefault.setVisibility(GONE);
                errorImage.setBackground(drawable);
                errorImage.setVisibility(VISIBLE);
                break;
            default:
                message = getResources().getString(R.string.something_went_wrong);
                drawable = context.getDrawable(R.drawable.ic_error_default);
                errorImage.setVisibility(GONE);
                errorImageDefault.setBackground(drawable);
                errorImageDefault.setVisibility(VISIBLE);
                break;
        }
        errorText.setText(message);
    }

    public int getLayoutRes() {
        return R.layout.error_view;
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initControl();
    }
}
