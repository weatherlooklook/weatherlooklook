package org.techtown.registerlogin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class Menu_home_allcodi extends Activity {

    private int[] imageIDs = new int[] {
            R.drawable.allcodi,
            R.drawable.allcodi2,
            R.drawable.allcodi3,
            R.drawable.allcodi4,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_home_allcodi);

        GridView gridViewImages = (GridView)findViewById(R.id.gridViewImages);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this, imageIDs);
        gridViewImages.setAdapter(imageGridAdapter);
    }
}
