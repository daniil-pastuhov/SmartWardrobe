package by.genlife.smartwardrobe.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public CustomViewGroup(Context context) {
        super(context);

        this.setOrientation(VERTICAL);

        this.textView = new TextView(context);
        this.imageView = new ImageView(context);
        this.button = new Button(context);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.textView.setLayoutParams(layoutParams);
        this.imageView.setLayoutParams(layoutParams);
        this.button.setLayoutParams(layoutParams);

        this.textView.setGravity(Gravity.CENTER);

        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setAdjustViewBounds(true);

        this.button.setText("Надеть");
        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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