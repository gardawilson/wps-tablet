package com.example.myapplication.sidebar;

import java.util.List;

public class SidebarSection {
    public final String title;
    public final List<SidebarNavItem> items;

    public SidebarSection(String title, List<SidebarNavItem> items) {
        this.title = title;
        this.items = items;
    }
}
