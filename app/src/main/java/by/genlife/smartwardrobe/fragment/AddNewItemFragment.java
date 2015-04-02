package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.Utils;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;

/**
 * Created by NotePad.by on 02.04.2015.
 */
public class AddNewItemFragment extends Fragment implements Constants {
    public static final String TAG = AddNewItemFragment.class.getSimpleName();
    Context context;
    ImageView thumbnail;
    Button addItem;
    Spinner categories;
    Bitmap curPhoto;
    EditText tmin, tmax, name;

    public AddNewItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adding_item, container, false);
        context = inflater.getContext();
        rootView.findViewById(R.id.btnPhotoAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        thumbnail = (ImageView) rootView.findViewById(R.id.imageViewAdd);
        addItem = (Button) rootView.findViewById(R.id.buttonToWardrobeAdd);
        categories = (Spinner) rootView.findViewById(R.id.category_spinner);
        tmin = (EditText) rootView.findViewById(R.id.min_temp);
        tmax = (EditText) rootView.findViewById(R.id.max_temp);
        name = (EditText) rootView.findViewById(R.id.name);
        final LinearLayout styleLayout = (LinearLayout) rootView.findViewById(R.id.style_layout);
        for (final String styleStr: Style.getStylesStr()) {
            styleLayout.addView(new CheckBox(context) {{setText(styleStr);}});
        }
        categories.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Category.getCategories()));
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = saveToFile(curPhoto);
                    Integer min = Integer.parseInt(tmin.getText().toString());
                    Integer max = Integer.parseInt(tmax.getText().toString());
                    HashSet<Style> styles = new HashSet<>();
                    for (int i = 0; i < styleLayout.getChildCount(); ++i) {
                        styles.add(Style.getStyle(((CheckBox)styleLayout.getChildAt(i)).getText().toString()));
                    }
                    Apparel apparel = new Apparel(path, name.getText().toString(), "Зелёная",
                            Category.getByType(categories.getSelectedItem().toString()), styles, new LinkedList<String>() {{
                        add("Красивый");
                    }}, min, max, "25-06-1994", "25-06-1994");
                    WardrobeManager.getInstance(context).addApparel(apparel);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        return rootView;
    }

    private String saveToFile(Bitmap curPhoto) throws IOException {
        File file = createImageFile();
        FileOutputStream fOut = new FileOutputStream(file);
        curPhoto.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
        return file.getAbsolutePath();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    curPhoto = (Bitmap) extras.get("data");
                    thumbnail.setImageBitmap(curPhoto);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
    }

    private String randomName() {
        String uuid = UUID.randomUUID().toString().substring(0, 7);
        return uuid;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String imageFileName = randomName() + timeStamp + "_";
        File storageDir = new File(Utils.getHomeDirectory());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}