package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 11/22/16.
 */

public interface OnRequestListener{
    public enum RequestType{
        CheckEmail,
        Login,
        Register,
        CheckDisplayName,
        CheckToken,
        GetItems,
        MakeConversation,
        GetMyUserDetails,
        GetConversations,
        PostItem,
        GetAllMessages,
        SendMessage,
        UploadImage,
        UpdateItem,
        UpdatePassword,
        UpdatePicture
    }
    public void onResponseReceived(RequestType type, Response response);

    public void onResponsesReceived(RequestType type, int resCode, Response[] response);
}
