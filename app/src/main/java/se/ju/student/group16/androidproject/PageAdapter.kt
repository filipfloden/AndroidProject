package se.ju.student.group16.androidproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> (return EventFragment())
            1-> (return FriendsFragment())
            2-> (return SettingsFragment())
            else->(return EventFragment())

        }
    }
}
