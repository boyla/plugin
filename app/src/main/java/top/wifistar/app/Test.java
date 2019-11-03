//package top.wifistar.app;
//
///**
// * Created by hasee on 2017/3/8.
// * <p>
// * Automatically generated file. DO NOT MODIFY
// */
//
//import android.os.Handler;
//import android.os.Message;
//
//import Observable;
//import rx.Observer;
//import rx.functions.Action1;
//import rx.functions.Func1;
//
//
//public final class Test {
//
//    public static void main(String[] args) {
//        Observer<Object> observer = new Observer<Object>() {
//
//            @Override
//            public void onCompleted() {
//                System.out.println("Finished");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object s) {
//                System.out.println(s);
//            }
//        };
//
//        Observable observable = Observable.just("One", "Two", "three");
//        //observable.subscribe(observer);
//
////        Observable.just(1,2,3,4,5).map(new Func1<Integer, String>() {
////            @Override
////            public String call(Integer integer) {
////                return "This is "+ integer;
////            }
////        }).subscribe(new Action1<String>() {
////            @Override
////            public void call(String s) {
////                System.out.println(s);
////            }
////        });
//
//        Integer[] nums = {1, 2, 3, 4, 5};
//        Observable.from(nums)
//                .filter(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer community) {
//                        return community > 3;
//                    }
//                }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer community) {
//                System.out.println(community);
//            }
//        });
//
//        Handler handler = new Handler();
//        Message m = Message.obtain();
//    }
//}
//
