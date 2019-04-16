package net.lzzy.practicesonline.activities.constants;


import net.lzzy.practicesonline.activities.utils.AppUtils;

public class ApiConnstants {

    private static final String IP= AppUtils.loadServerSetting(AppUtils.getContext()).first;
    private static final String PORT=AppUtils.loadServerSetting(AppUtils.getContext()).second;
    private static final String PROTOCOL="http://";

    /**
     * API地址
     */
    public static final String URL_API=PROTOCOL.concat(IP).concat(":").concat(PORT);
}