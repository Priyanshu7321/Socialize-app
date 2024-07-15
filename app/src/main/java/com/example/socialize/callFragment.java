package com.example.socialize;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;
import java.util.List;

public class callFragment extends Fragment {


    public callFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_call, container, false);
        // need a activityContext.
       /* PermissionX.init(callFragment.this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
                    }
                }).request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                                         @NonNull List<String> deniedList) {
                    }
                });
//        Application application = (Application) getContext(); // Android's application context
        long appID = 774966427;   // yourAppID
        String appSign ="837e60fb0097f5b668a9073ae2807f8b2d1518846fb315f2972155ee7c14c3ca";  // yourAppSign
        String userID ="User1"; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName ="Baki";   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getActivity().getApplication(), appID, appSign, userID, userName,callInvitationConfig);
        String targetUserID = "User2"; // The ID of the user you want to call.
        String targetUserName = "Goku"; // The username of the user you want to call.
        Context context = getContext(); // Android context.

        ZegoSendCallInvitationButton button = new ZegoSendCallInvitationButton(context);
        button.setIsVideoCall(true);
        button.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        button.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));
*/
        return v;
    }
}