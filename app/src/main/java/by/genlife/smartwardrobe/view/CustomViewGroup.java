package by.genlife.smartwardrobe.view;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.genlife.smartwardrobe.R;

/**
 * Created by NotePad.by on 07.05.2015.
 */
public class CustomViewGroup extends LinearLayout {

    // =============================================================================
    // Child views
    // =============================================================================

    private TextView textView;
    private ImageView imageView;
    private Button button;

    // =============================================================================
    // Constructor
    // =============================================================================

    public CustomViewGroup(Context context, OnClickListener listener) {
        super(context);
        this.setOrientation(VERTICAL);

        this.textView = new TextView(context);
        this.imageView = new ImageView(context);
        this.button = new Button(context);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.textView.setLayoutParams(layoutParams);
        this.imageView.setLayoutParams(layoutParams);
        this.button.setLayoutParams(layoutParams);

        this.textView.setGravity(Gravity.CENTER);

        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setAdjustViewBounds(true);

        this.button.setText(context.getString(R.string.button_put_on));
        this.button.setOnClickListener(listener);

        this.addView(this.textView);
        this.addView(this.imageView);
        this.addView(this.button);
    }

    // =============================================================================
    // Getters
    // =============================================================================

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}