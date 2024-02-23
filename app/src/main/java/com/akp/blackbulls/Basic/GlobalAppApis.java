package com.akp.blackbulls.Basic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GlobalAppApis {
    public String Login(String MobileNumber,String Password) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MobileNumber", MobileNumber);
            jsonObject1.put("Action", "Action");
            jsonObject1.put("Password", Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Registartion(String ref, String mnane, String mob,String email,String pass,String cpass) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("RefrerralId", ref);
            jsonObject1.put("MemberName", mnane);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("EmialId", email);
            jsonObject1.put("Password", pass);
            jsonObject1.put("ConfirmPassword", cpass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String PaymentaddEMI( ArrayList<String> arrRepaymentListt , String selectedName){
        JSONObject jsonObject1 = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            jsonObject1.put("AccountNo", selectedName);
            for (int arrayy = 0; arrayy < arrRepaymentListt.size(); arrayy++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("InstallmentNo", arrRepaymentListt.get(arrayy));
                array.put(jsonObject);

            }
            jsonObject1.put("objemi",array);

            Log.v("DADADADA", String.valueOf(jsonObject1));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String ChangePassword(String newpass, String oldpass, String uid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("NewPassword", newpass);
            jsonObject1.put("OldPassword", oldpass);
            jsonObject1.put("Userid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Dashboard(String action, String pakid, String pass,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Packageid", pakid);
            jsonObject1.put("Password", pass);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String UPITransfer(String amount, String custmob, String custname,String upiid,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Amount", amount);
            jsonObject1.put("CustomerMobileNo", custmob);
            jsonObject1.put("Name", custname);
            jsonObject1.put("UPIID", upiid);
            jsonObject1.put("UserId", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String PostAddSenderAPI(String add, String fname, String lname, String mob, String pincode, String state) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Address", add);
            jsonObject1.put("FirstName", fname);
            jsonObject1.put("LastName", lname);
            jsonObject1.put("MobileNo", mob);
            jsonObject1.put("PinCode", pincode);
            jsonObject1.put("State", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String PostGetCustomerAPI(String mob) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MobileNo", mob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetProviderList() {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "");
            jsonObject1.put("ProviderId", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetWallletAmount(String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", "");
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String Operator(String action,String providerid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("ProviderId", providerid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String PostRechargeAPI(String Amount, String Circle, String MobileNo, String UserId,String optr) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Amount", Amount);
            jsonObject1.put("Circle", Circle);
            jsonObject1.put("MobileNo", MobileNo);
            jsonObject1.put("UserId", UserId);
            jsonObject1.put("optr", optr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String TransactionReporty(String ac,String rtype, String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", ac);
            jsonObject1.put("ReportType", rtype);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



//    public String TransactionReportHistory(String fromdate, String memberid, String reporttype, String status, String todate, String transactiontype) {
//        JSONObject jsonObject1 = new JSONObject();
//        try {
//            jsonObject1.put("FromDate", fromdate);
//            jsonObject1.put("MemberId", memberid);
//            jsonObject1.put("ReportType", reporttype);
//            jsonObject1.put("Status", status);
//            jsonObject1.put("ToDate", todate);
//            jsonObject1.put("TransactionType", transactiontype);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject1.toString();
//    }



    public String Profile(String UserId) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("UserId", UserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String Wallet(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String TopupDetails(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String MyReferral(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String MyTeam(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String DirectIncomeAPI(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String LevelIncomeAPI(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String ContractIncomeAPI(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ShowWithdrwarAPI(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String WalletHistoryAPI(String action,String userid) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("Action", action);
            jsonObject1.put("Userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String Otpverify(String mobileNo, String otp) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("MobileNo", mobileNo);
            jsonObject1.put("OTP", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



}

