package com.example.myapplication.sidebar;

import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class SidebarNavItem {
    public final int id;
    public final String label;
    public final int iconRes;
    public final String permissionKey;
    public final Class<?> targetActivity;
    /**
     * Pabrik Fragment untuk layar yang sudah dimigrasi ke arsitektur
     * Single-Activity (HostActivity). Bila non-null, item ini dibuka sebagai
     * Fragment di dalam HostActivity, bukan sebagai Activity terpisah.
     */
    public final Supplier<Fragment> fragmentFactory;
    public final boolean isLogout;
    public final List<SidebarNavItem> children;

    private SidebarNavItem(int id, String label, int iconRes, String permissionKey,
                            Class<?> targetActivity, Supplier<Fragment> fragmentFactory,
                            boolean isLogout, List<SidebarNavItem> children) {
        this.id = id;
        this.label = label;
        this.iconRes = iconRes;
        this.permissionKey = permissionKey;
        this.targetActivity = targetActivity;
        this.fragmentFactory = fragmentFactory;
        this.isLogout = isLogout;
        this.children = children;
    }

    public static SidebarNavItem flat(int id, String label, int iconRes, Class<?> targetActivity) {
        return new SidebarNavItem(id, label, iconRes, null, targetActivity, null, false, Collections.emptyList());
    }

    public static SidebarNavItem flatFragment(int id, String label, int iconRes, Supplier<Fragment> fragmentFactory) {
        return new SidebarNavItem(id, label, iconRes, null, null, fragmentFactory, false, Collections.emptyList());
    }

    public static SidebarNavItem logout(int id, String label, int iconRes) {
        return new SidebarNavItem(id, label, iconRes, null, null, null, true, Collections.emptyList());
    }

    public static SidebarNavItem leafChild(int id, String label, int iconRes, Class<?> targetActivity, String permissionKey) {
        return new SidebarNavItem(id, label, iconRes, permissionKey, targetActivity, null, false, Collections.emptyList());
    }

    public static SidebarNavItem leafChildFragment(int id, String label, int iconRes,
                                                   Supplier<Fragment> fragmentFactory, String permissionKey) {
        return new SidebarNavItem(id, label, iconRes, permissionKey, null, fragmentFactory, false, Collections.emptyList());
    }

    public static SidebarNavItem parent(int id, String label, int iconRes, List<SidebarNavItem> children) {
        return new SidebarNavItem(id, label, iconRes, null, null, null, false, children);
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean hasFragment() {
        return fragmentFactory != null;
    }

    public boolean isNavigable() {
        return targetActivity != null || fragmentFactory != null || isLogout;
    }
}
