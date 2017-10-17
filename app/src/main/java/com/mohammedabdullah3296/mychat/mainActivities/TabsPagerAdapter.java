package com.mohammedabdullah3296.mychat.mainActivities;

/**
 * Created by Mohammed Abdullah on 9/3/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mohammedabdullah3296.mychat.fragments.ChatsFragment;
import com.mohammedabdullah3296.mychat.fragments.FriendsFragment;
import com.mohammedabdullah3296.mychat.fragments.RequestsFragment;

class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return "requests  ";
            case 1:
                return "  chats   ";
            case 2:

                return " friends  ";
            default:
                return null;
        }
    }
}
