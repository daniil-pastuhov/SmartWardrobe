package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.Utils;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.constants.Tags;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

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
    EditText tmin, tmax, name, color, tags;
    LinearLayout styleLayout;
    boolean[] styleArr;
    boolean isEditMode = false;

    public AddNewItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adding_item, container, false);
        context = inflater.getContext();
        if (savedInstanceState == null) {
            styleArr = new boolean[Style.size()];
        } else {
            styleArr = savedInstanceState.getBooleanArray(STATE_STYLES_CHECKED);
        }
        rootView.findViewById(R.id.btnPhotoAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        thumbnail = (ImageView) rootView.findViewById(R.id.imageViewAdd);
        thumbnail.setOnClickListener(new View.OnClickListener() {
            int rotation = 90;

            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                imageView.setRotation(rotation);
                rotation += 90;
            }
        });
        addItem = (Button) rootView.findViewById(R.id.buttonToWardrobeAdd);
        categories = (Spinner) rootView.findViewById(R.id.category_spinner);
        tmin = (EditText) rootView.findViewById(R.id.min_temp);
        tmax = (EditText) rootView.findViewById(R.id.max_temp);
        name = (EditText) rootView.findViewById(R.id.name);
        color = (EditText) rootView.findViewById(R.id.color);
        tags = (EditText) rootView.findViewById(R.id.etTags);
        styleLayout = (LinearLayout) rootView.findViewById(R.id.style_layout);
        for (int i = 0; i < Style.size(); ++i) {
            final int k = i;
            styleLayout.addView(new CheckBox(context) {
                {
                    setText(Style.values()[k].getDescription());
                    setChecked(styleArr[k]);
                }
            });
        }

        categories.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Category.getCategories()));
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = saveToFile(curPhoto);
                    Integer min = Integer.parseInt(tmin.getText().toString());
                    Integer max = Integer.parseInt(tmax.getText().toString());
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String date = format.format(new Date());
                    translateStylesToArray();
                    HashSet<Style> styles = new HashSet<>();
                    for (int i = 0; i < styleLayout.getChildCount(); ++i) {
                        if (styleArr[i])
                            styles.add(Style.getStyle(((CheckBox) styleLayout.getChildAt(i)).getText().toString()));
                    }
                    Apparel apparel = new Apparel(path, name.getText().toString(), color.getText().toString(),
                            Category.getByType(categories.getSelectedItem().toString()), styles, getTags(tags.getText().toString()),
                            min, max, date, date, 0, Constants.homeRepository);
                    WardrobeManager.getInstance(context, OnTaskCompleteListener.<Void>getEmptyListener()).addApparel(apparel);
                    reset();
                    hideKeyboard();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        isEditMode();
        return rootView;
    }

    private void isEditMode() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(EXTRA_APPAREL)) {
            isEditMode = true;
            Apparel apparel = args.getParcelable(EXTRA_APPAREL);
            thumbnail.setImageURI(Uri.parse("file:\\" + apparel.getImagePath()));
            thumbnail.setVisibility(View.VISIBLE);
            tmin.setText(apparel.getMinT().toString());
            tmax.setText(apparel.getMaxT().toString());
            name.setText(apparel.getName());
            color.setText(apparel.getColor());
            tags.setText(Tags.parseToString(apparel.getTags()));
        }
    }

    private void reset() {
        thumbnail.setVisibility(View.GONE);
        categories.setSelection(0);
        curPhoto = null;
        resetEditTexts(tmin, tmax, name, color, tags);
        for (int i = 0; i < styleLayout.getChildCount(); ++i) {
            ((CheckBox) styleLayout.getChildAt(i)).setChecked(false);
        }
    }

    private void resetEditTexts(EditText... editTexts) {
        for (EditText et : editTexts) {
            et.setText("");
        }
    }

    private List<String> getTags(String s) {
        ArrayList<String> res = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens())
            res.add(st.nextToken().toLowerCase());
        return res;
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
                    thumbnail.setVisibility(View.VISIBLE);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        translateStylesToArray();
        outState.putBooleanArray(STATE_STYLES_CHECKED, styleArr);
    }

    private void translateStylesToArray() {
        for (int i = 0; i < styleLayout.getChildCount(); ++i) {
            styleArr[i] = ((CheckBox) styleLayout.getChildAt(i)).isChecked();
        }
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

    private void hideKeyboard() {
        Activity activity = getActivity();
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}