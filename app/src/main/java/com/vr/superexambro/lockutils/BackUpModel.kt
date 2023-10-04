package com.vr.superexambro.lockutils

class BackUpModel {
    var appPackage: String? = null
    var newAppPackage: String? = null
    var inter: String? = null
    var fb_inter: String? = null
    var fb_native: String? = null
    var fb_banner: String? = null
    var banner: String? = null
    var nativeAd: String? = null
    var rewarded: String? = null
    var isLive: Boolean? = null
    var isAdsShow: Boolean? = null
    var isShowGG: Boolean? = null
    var percent_banner = 0
    var percent_inter = 0
    var percent_native = 0
    var numberNativeAd = 0
    var numberInterAd = 0
    var timeRequestAd = 0

    constructor(
        appPackage: String?,
        newAppPackage: String?,
        inter: String?,
        fb_inter: String?,
        fb_native: String?,
        fb_banner: String?,
        banner: String?,
        nativeAd: String?,
        rewarded: String?,
        isLive: Boolean?,
        isAdsShow: Boolean?,
        isShowGG: Boolean?,
        percent_banner: Int,
        percent_inter: Int,
        percent_native: Int,
        numberNativeAd: Int,
        numberInterAd: Int,
        timeRequestAd: Int
    ) {
        this.appPackage = appPackage
        this.newAppPackage = newAppPackage
        this.inter = inter
        this.fb_inter = fb_inter
        this.fb_native = fb_native
        this.fb_banner = fb_banner
        this.banner = banner
        this.nativeAd = nativeAd
        this.rewarded = rewarded
        this.isLive = isLive
        this.isAdsShow = isAdsShow
        this.isShowGG = isShowGG
        this.percent_banner = percent_banner
        this.percent_inter = percent_inter
        this.percent_native = percent_native
        this.numberNativeAd = numberNativeAd
        this.numberInterAd = numberInterAd
        this.timeRequestAd = timeRequestAd
    }

    constructor()
}