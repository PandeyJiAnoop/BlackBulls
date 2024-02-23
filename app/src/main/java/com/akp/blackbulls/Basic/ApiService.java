package com.akp.blackbulls.Basic;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("Login")
    Call<String> getLogin(
            @Body String body);
    @POST("Registration")
    Call<String> signupAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("LoanEmiRepayment")
    Call<String> PaymentaddEMI(
            @Body String body);


    @POST("ChangePassword")
    Call<String> changepassAPI(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Dashboard")
    Call<String> getDashboard(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("UPITransfer")
    Call<String> getUPITransfer(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ProfileView")
    Call<String> getProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("TopupDetails")
    Call<String> getTopupDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetWallet")
    Call<String> getWallet(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MyRefferal")
    Call<String> getReferral(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("MyTeam")
    Call<String> getMyTeam(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("DirectIncome")
    Call<String> getDirectincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("LevelIncome")
    Call<String> getLevelincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ROIBonus")
    Call<String> getContractincome(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("OpenWithdrawal")
    Call<String> getwithdrwar(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("WithdrawalHistory")
    Call<String> getWalletHistory(
            @Body String body);

    @Headers("Content-Type: application/json")
    @GET("GetServiceList")
    Call<String> OperatorList();

    @Headers("Content-Type: application/json")
    @POST("GetProviderList")
    Call<String> GetProviderListService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetWallet")
    Call<String> GetWalletService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetOpratorList")
    Call<String> OperatorService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("PostRecharge")
    Call<String> PostRecharge(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("AddSender")
    Call<String> AddSenderAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("Otpverify")
    Call<String> Otpverify(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("GetCustomer")
    Call<String> GetCustomerAPI(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("AllTransHistory")
    Call<String> TransactionReportAPI(
            @Body String body);
}