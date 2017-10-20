import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.captain.schedit.Tab1;
import com.example.captain.schedit.Tab2;
import com.example.captain.schedit.Tab3;

/**
 * Created by saavf on 10/23/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                    Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;

            default:
            return null;
        }

    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
