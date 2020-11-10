package com.aciv.parceros.Interfaces;

import com.aciv.parceros.Models.Database.Comment;
import com.aciv.parceros.Models.Database.Location;
import com.aciv.parceros.Models.Database.Phone;
import com.aciv.parceros.Models.Database.Profession;
import com.aciv.parceros.Models.Database.APIResponse;
import com.aciv.parceros.Models.Database.Profile;
import com.aciv.parceros.Models.Database.Reaction;
import com.aciv.parceros.Models.Database.Schedule;
import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.Models.Database.SignupUser;
import com.aciv.parceros.Models.Database.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("professions")
    Call<ArrayList<Profession>> getProfessions();

    @POST("user/signup")
    Call<APIResponse> signupUser(@Body SignupUser signupUser);

    @Multipart
    @POST("user/picture/store")
    Call<APIResponse> updateProfilePicture(@Part MultipartBody.Part photo, @Part("user_id") long user_id);

    @GET("user/search/{text}")
    Call<ArrayList<User>> searchUsers(@Path("text") String text);

    @GET("user/show/{id}")
    Call<User> showUser(@Path("id") long id);

    @GET("user/{id}/services")
    Call<ArrayList<Service>> showServicesByUser(@Path("id") long id);

    @GET("user/{id}/schedules")
    Call<ArrayList<Schedule>> showSchedulesByUser(@Path("id") long id);

    @GET("user/{id}/comments_for_me")
    Call<ArrayList<Comment>> showCommentsByUserCreatedTo(@Path("id") long id);

    @GET("user/{id}/location")
    Call<Location> showLocationByUser(@Path("id") long id);

    @GET("user/{user_service}/services/reactions/user/{user_reaction}")
    Call<ArrayList<Service>> showServicesWithReactionsByUser(@Path("user_service") long user_service, @Path("user_reaction") long user_reaction);

    @POST("reaction/create")
    Call<APIResponse> createServiceReaction(@Body Reaction reaction);

    @POST("reaction/update")
    Call<APIResponse> updateServiceReaction(@Body Reaction reaction);

    @POST("reaction/delete/{id}")
    Call<APIResponse> deleteServiceReaction(@Path("id") long id);

    @POST("user/auth_credentials")
    Call<APIResponse> authUser(@Body Phone phone);
}
