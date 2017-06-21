package com.store.controller;

import com.core.json.JsonResponse;
import com.google.common.collect.Lists;
import com.param.CompanyConfigParam;
import com.param.PortalParam;
import com.param.PortalParamManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by laizy on 2017/6/12.
 */
@RequestMapping("/companyConfig")
@Controller
public class CompanyConfigController extends BaseController {
    @Autowired
    private PortalParamManage portalParamManage;

    @RequestMapping("/config")
    public String config(Model model) {
        Map<String, PortalParam> paramMap = portalParamManage.mapByKey(CompanyConfigParam.class);
        CompanyConfigBO configBO = new CompanyConfigBO();
        configBO.phone = paramMap.get(CompanyConfigParam.PHONE_PARAM_KEY);
        configBO.address = paramMap.get(CompanyConfigParam.ADDRESS_PARAM_KEY);
        configBO.wechat = paramMap.get(CompanyConfigParam.WECHAT_PARAM_KEY);
        configBO.weibo = paramMap.get(CompanyConfigParam.WEIBO_PARAM_KEY);
        configBO.tmall = paramMap.get(CompanyConfigParam.TMALL_PARAM_KEY);
        configBO.aboutUs = paramMap.get(CompanyConfigParam.ABOUTUS_PARAM_KEY);
        configBO.introduction = paramMap.get(CompanyConfigParam.ABOUTUSINTRO_PARAM_KEY);
        configBO.contactUs = paramMap.get(CompanyConfigParam.CONTACTUS_PARAM_KEY);
        configBO.contactUsContent = paramMap.get(CompanyConfigParam.CONTACTUSCONTENT_PARAM_KEY);
        model.addAttribute("config", configBO);
        return "/store/companyConfig/form";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modify(CompanyConfigBO configBO) {
        portalParamManage.modifyParams(configBO.list());
        return JsonResponse.JSON_SUCCESS;
    }

    public static class CompanyConfigBO {
        private PortalParam phone;
        private PortalParam address;
        private PortalParam wechat;
        private PortalParam weibo;
        private PortalParam tmall;
        private PortalParam aboutUs;
        private PortalParam introduction;
        private PortalParam contactUs;
        private PortalParam contactUsContent;

        public PortalParam getPhone() {
            return phone;
        }

        public void setPhone(PortalParam phone) {
            this.phone = phone;
        }

        public PortalParam getAddress() {
            return address;
        }

        public void setAddress(PortalParam address) {
            this.address = address;
        }

        public PortalParam getWechat() {
            return wechat;
        }

        public void setWechat(PortalParam wechat) {
            this.wechat = wechat;
        }

        public PortalParam getWeibo() {
            return weibo;
        }

        public void setWeibo(PortalParam weibo) {
            this.weibo = weibo;
        }

        public PortalParam getTmall() {
            return tmall;
        }

        public void setTmall(PortalParam tmall) {
            this.tmall = tmall;
        }

        public PortalParam getAboutUs() {
            return aboutUs;
        }

        public void setAboutUs(PortalParam aboutUs) {
            this.aboutUs = aboutUs;
        }

        public PortalParam getIntroduction() {
            return introduction;
        }

        public void setIntroduction(PortalParam introduction) {
            this.introduction = introduction;
        }

        public PortalParam getContactUs() {
            return contactUs;
        }

        public void setContactUs(PortalParam contactUs) {
            this.contactUs = contactUs;
        }

        public PortalParam getContactUsContent() {
            return contactUsContent;
        }

        public void setContactUsContent(PortalParam contactUsContent) {
            this.contactUsContent = contactUsContent;
        }

        public List<PortalParam> list() {
            return Lists.newArrayList(phone, address, wechat, weibo, tmall, aboutUs, introduction, contactUs, contactUsContent);
        }
    }
}
