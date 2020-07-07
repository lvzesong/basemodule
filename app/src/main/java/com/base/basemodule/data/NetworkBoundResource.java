package com.base.basemodule.data;


// ResultType: Type for the DataStatus data
// RequestType: Type for the API response
public abstract class NetworkBoundResource<ResultType, RequestType>
{

//    Called to save the result of the API response into the database
//    当要把网络数据存储到数据库中时调用
//    @WorkerThread
//    protected abstract void saveCallResult(@NonNull RequestType item);
//    Called with the data in the database to decide whether it should be
//    fetched from the network.
//    决定是否去网络获取数据
//    @MainThread
//    protected abstract boolean shouldFetch(@Nullable ResultType data);
   // Called to get the cached data from the database
    //用于从数据库中获取缓存数据
//    @NonNull @MainThread
//    protected abstract LiveData<ResultType> loadFromDb();
    //Called to create the API call.
//    @NonNull @MainThread
//    protected abstract LiveData<ApiResponse<RequestType>> createCall();
    // 创建网络数据请求
//    @NonNull @MainThread
//    protected abstract LiveData<RequestType> createCall();
//    Called when the fetch fails. The child class may want to reset components
//    like rate limiter.
    //网络数据获取失败时调用
//    @MainThread
//    protected void onFetchFailed() {
//    }
   // returns a LiveData that represents the resource
   // public abstract  LiveData<LiveStutas<ResultType>> getAsLiveData() ;


}