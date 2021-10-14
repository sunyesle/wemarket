package com.sys.market.controller;

import com.sys.market.config.security.JwtTokenProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping("/")
    public ModelAndView main(ModelAndView mv){
        mv.setViewName("main");
        return mv;
    }

    @GetMapping("/detail/{itemId}")
    public ModelAndView detail(ModelAndView mv, @PathVariable String itemId){
        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", itemId);

        mv.addObject("data", data);
        mv.setViewName("detail");
        return mv;
    }

    @GetMapping("/write")
    public ModelAndView write(ModelAndView mv){

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", "");

        mv.addObject("data", data);
        mv.setViewName("write");
        return mv;
    }

    @GetMapping("/write/{itemId}")
    public ModelAndView writeItem(ModelAndView mv, @PathVariable String itemId){
        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", itemId);

        mv.addObject("data", data);
        mv.setViewName("write");
        return mv;
    }

    @GetMapping("/chat")
    public ModelAndView chat(ModelAndView mv){
        mv.setViewName("chat");
        return mv;
    }

    @GetMapping("/chat/{userId}")
    public ModelAndView chat(ModelAndView mv, @PathVariable String userId,
                             @CookieValue(name = JwtTokenProvider.ACCESS_TOKEN_NAME, required = false) String accessToken){
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);
        data.put("accessToken", accessToken);

        mv.addObject("data", data);
        mv.setViewName("chat-detail");
        return mv;
    }

    @GetMapping("/signin")
    public ModelAndView signin(ModelAndView mv){
        mv.setViewName("signin");
        return mv;
    }

    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mv){
        mv.setViewName("signup");
        return mv;
    }

    @GetMapping("/profile/{userId}/listing")
    public ModelAndView profileListing(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/listing");
        return mv;
    }
    @GetMapping("/profile/{userId}/offer")
    public ModelAndView profileOffer(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/offer");
        return mv;
    }
    @GetMapping("/profile/{userId}/myoffer")
    public ModelAndView profileMyoffer(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/myoffer");
        return mv;
    }
    @GetMapping("/profile/{userId}/wishlist")
    public ModelAndView profileWishlist(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/wishlist");
        return mv;
    }
    @GetMapping("/profile/{userId}/following")
    public ModelAndView profileFollowing(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/following");
        return mv;
    }
    @GetMapping("/profile/{userId}/review")
    public ModelAndView profileReview(ModelAndView mv, @PathVariable String userId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);

        mv.addObject("data", data);
        mv.setViewName("profile/review");
        return mv;
    }

    @GetMapping("/setting")
    public ModelAndView setting(ModelAndView mv) {
        mv.setViewName("setting/main");
        return mv;
    }
    @GetMapping("/setting/image")
    public ModelAndView settingImage(ModelAndView mv) {
        mv.setViewName("setting/image");
        return mv;
    }
    @GetMapping("/setting/nickname")
    public ModelAndView settingNickname(ModelAndView mv) {
        mv.setViewName("setting/nickname");
        return mv;
    }
    @GetMapping("/setting/address")
    public ModelAndView settingAddress(ModelAndView mv) {
        mv.setViewName("setting/address");
        return mv;
    }
    @GetMapping("/setting/email")
    public ModelAndView settingEmail(ModelAndView mv) {
        mv.setViewName("setting/email");
        return mv;
    }
    @GetMapping("/setting/bio")
    public ModelAndView settingBio(ModelAndView mv) {
        mv.setViewName("setting/bio");
        return mv;
    }
    @GetMapping("/setting/password")
    public ModelAndView settingPassword(ModelAndView mv) {
        mv.setViewName("setting/password");
        return mv;
    }
    @GetMapping("/setting/account")
    public ModelAndView settingAccount(ModelAndView mv) {
        mv.setViewName("setting/account");
        return mv;
    }

    @GetMapping("/verify-email")
    public ModelAndView verifyEmail(ModelAndView mv) {
        mv.setViewName("verify-email");
        return mv;
    }

    @GetMapping("/verify/{code}")
    public ModelAndView verifyEmailSuccess(ModelAndView mv, @PathVariable String code) {
        HashMap<String, String> data = new HashMap<>();
        data.put("code", code);

        mv.addObject("data", data);
        mv.setViewName("verify");
        return mv;
    }

    @GetMapping("/delete-account-success")
    public ModelAndView deleteAccountSuccess(ModelAndView mv) {
        mv.setViewName("delete-account-success");
        return mv;
    }

    @GetMapping("/intro")
    public ModelAndView intro(ModelAndView mv) {
        mv.setViewName("intro");
        return mv;
    }

    @GetMapping("/help")
    public ModelAndView help(ModelAndView mv) {
        mv.setViewName("help");
        return mv;
    }
}
