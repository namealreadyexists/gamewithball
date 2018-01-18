package com.example.transcend.gamewithball;

import android.support.v4.app.Fragment;

public class SceneActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return SceneFragment.newInstance();
    }
}
