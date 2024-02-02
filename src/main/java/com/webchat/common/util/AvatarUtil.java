package com.webchat.common.util;

import java.util.Random;

/**
 * @Author: by loks666 on GitHub
 * @Date: 2021-7-13 0013 23:42
 * @Description: 无描述信息
 */

public class AvatarUtil {


    private static final String [] avatarArray = new String[] {
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/1.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/2.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/3.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/4.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/5.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/6.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/7.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/8.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/9.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/10.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/11.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/12.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/13.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/14.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/15.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/16.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/17.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/18.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/19.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/20.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/21.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/22.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/23.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/24.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/25.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/26.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/27.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/28.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/29.png",
            "https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/30.png"
    };

    /***
     * 随机获取一个头像
     * @return
     */
    public static String getRandomAvatar() {
        int index = new Random().nextInt(30);
        return avatarArray[index];
    }
}
