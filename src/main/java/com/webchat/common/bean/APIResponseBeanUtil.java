package com.webchat.common.bean;

public class APIResponseBeanUtil {

    /***
     * 构建成功结果
     * @param data
     * @return
     */
    public static APIResponseBean success(Object data) {
        APIResponseBean apiResponseBean = new APIResponseBean();
        apiResponseBean.setSuccess(true);
        apiResponseBean.setCode(200);
        apiResponseBean.setMsg("OK");
        apiResponseBean.setData(data);
        return apiResponseBean;
    }

    public static APIResponseBean success() {
        return success(null);
    }

    /***
     * 构建失败结果
     * @param code
     * @param msg
     * @return
     */
    public static APIResponseBean error(Integer code, String msg) {
        APIResponseBean apiResponseBean = new APIResponseBean();
        apiResponseBean.setSuccess(false);
        apiResponseBean.setCode(code);
        apiResponseBean.setMsg(msg);
        return apiResponseBean;
    }
}
