package top.wifistar.imagelib;

import java.io.Serializable;
import java.util.List;

/**
 * Created by boyla on 2019/9/8.
 */

public class UnsplashData implements Serializable{

    /**
     * id : u4gwRbr2_j0
     * created_at : 2019-08-30T09:10:41-04:00
     * updated_at : 2019-09-01T15:02:00-04:00
     * width : 4500
     * height : 3000
     * color : #AFBFC9
     * description : null
     * alt_description : selective focus photography of sittng gray tabby kitten
     * urls : {"raw":"https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjkwMjg4fQ","full":"https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjkwMjg4fQ","regular":"https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ","small":"https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ","thumb":"https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ"}
     * links : {"self":"https://api.unsplash.com/photos/u4gwRbr2_j0","html":"https://unsplash.com/photos/u4gwRbr2_j0","download":"https://unsplash.com/photos/u4gwRbr2_j0/download","download_location":"https://api.unsplash.com/photos/u4gwRbr2_j0/download"}
     * categories : []
     * likes : 87
     * liked_by_user : false
     * current_user_collections : []
     * user : {"id":"UmSr3GimnHA","updated_at":"2019-09-04T23:46:48-04:00","username":"ramche","name":"Ramiz Dedaković","first_name":"Ramiz","last_name":"Dedaković","twitter_username":null,"portfolio_url":"http://ramche.com/","bio":"I am 23 year old photographer and videographer. I love creating more and new stuff. Every day is a new story.","location":"Bosnia","links":{"self":"https://api.unsplash.com/users/ramche","html":"https://unsplash.com/@ramche","photos":"https://api.unsplash.com/users/ramche/photos","likes":"https://api.unsplash.com/users/ramche/likes","portfolio":"https://api.unsplash.com/users/ramche/portfolio","following":"https://api.unsplash.com/users/ramche/following","followers":"https://api.unsplash.com/users/ramche/followers"},"profile_image":{"small":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32","medium":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64","large":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"},"instagram_username":"ramche_","total_collections":0,"total_likes":39,"total_photos":257,"accepted_tos":true}
     * exif : {"make":"Canon","model":"Canon EOS R","exposure_time":"1/60","aperture":"2.0","focal_length":"35.0","iso":800}
     * location : {"title":"","name":null,"city":null,"country":null,"position":{"latitude":null,"longitude":null}}
     * views : 255618
     * downloads : 1164
     */

    private String id;
    private String created_at;
    private String updated_at;
    private int width;
    private int height;
    private String color;
    private Object description;
    private String alt_description;
    private UrlsBean urls;
    private LinksBean links;
    private int likes;
    private boolean liked_by_user;
    private UserBean user;
    private ExifBean exif;
    private LocationBean location;
    private int views;
    private int downloads;
    private List<?> categories;
    private List<?> current_user_collections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getAlt_description() {
        return alt_description;
    }

    public void setAlt_description(String alt_description) {
        this.alt_description = alt_description;
    }

    public UrlsBean getUrls() {
        return urls;
    }

    public void setUrls(UrlsBean urls) {
        this.urls = urls;
    }

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked_by_user() {
        return liked_by_user;
    }

    public void setLiked_by_user(boolean liked_by_user) {
        this.liked_by_user = liked_by_user;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ExifBean getExif() {
        return exif;
    }

    public void setExif(ExifBean exif) {
        this.exif = exif;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public List<?> getCategories() {
        return categories;
    }

    public void setCategories(List<?> categories) {
        this.categories = categories;
    }

    public List<?> getCurrent_user_collections() {
        return current_user_collections;
    }

    public void setCurrent_user_collections(List<?> current_user_collections) {
        this.current_user_collections = current_user_collections;
    }

    public static class UrlsBean {
        /**
         * raw : https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjkwMjg4fQ
         * full : https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjkwMjg4fQ
         * regular : https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ
         * small : https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ
         * thumb : https://images.unsplash.com/photo-1567170566706-e2d2db258886?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjkwMjg4fQ
         */

        private String raw;
        private String full;
        private String regular;
        private String small;
        private String thumb;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }

    public static class LinksBean {
        /**
         * self : https://api.unsplash.com/photos/u4gwRbr2_j0
         * html : https://unsplash.com/photos/u4gwRbr2_j0
         * download : https://unsplash.com/photos/u4gwRbr2_j0/download
         * download_location : https://api.unsplash.com/photos/u4gwRbr2_j0/download
         */

        private String self;
        private String html;
        private String download;
        private String download_location;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getDownload_location() {
            return download_location;
        }

        public void setDownload_location(String download_location) {
            this.download_location = download_location;
        }
    }

    public static class UserBean {
        /**
         * id : UmSr3GimnHA
         * updated_at : 2019-09-04T23:46:48-04:00
         * username : ramche
         * name : Ramiz Dedaković
         * first_name : Ramiz
         * last_name : Dedaković
         * twitter_username : null
         * portfolio_url : http://ramche.com/
         * bio : I am 23 year old photographer and videographer. I love creating more and new stuff. Every day is a new story.
         * location : Bosnia
         * links : {"self":"https://api.unsplash.com/users/ramche","html":"https://unsplash.com/@ramche","photos":"https://api.unsplash.com/users/ramche/photos","likes":"https://api.unsplash.com/users/ramche/likes","portfolio":"https://api.unsplash.com/users/ramche/portfolio","following":"https://api.unsplash.com/users/ramche/following","followers":"https://api.unsplash.com/users/ramche/followers"}
         * profile_image : {"small":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32","medium":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64","large":"https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"}
         * instagram_username : ramche_
         * total_collections : 0
         * total_likes : 39
         * total_photos : 257
         * accepted_tos : true
         */

        private String id;
        private String updated_at;
        private String username;
        private String name;
        private String first_name;
        private String last_name;
        private Object twitter_username;
        private String portfolio_url;
        private String bio;
        private String location;
        private LinksBeanX links;
        private ProfileImageBean profile_image;
        private String instagram_username;
        private int total_collections;
        private int total_likes;
        private int total_photos;
        private boolean accepted_tos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public Object getTwitter_username() {
            return twitter_username;
        }

        public void setTwitter_username(Object twitter_username) {
            this.twitter_username = twitter_username;
        }

        public String getPortfolio_url() {
            return portfolio_url;
        }

        public void setPortfolio_url(String portfolio_url) {
            this.portfolio_url = portfolio_url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public LinksBeanX getLinks() {
            return links;
        }

        public void setLinks(LinksBeanX links) {
            this.links = links;
        }

        public ProfileImageBean getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(ProfileImageBean profile_image) {
            this.profile_image = profile_image;
        }

        public String getInstagram_username() {
            return instagram_username;
        }

        public void setInstagram_username(String instagram_username) {
            this.instagram_username = instagram_username;
        }

        public int getTotal_collections() {
            return total_collections;
        }

        public void setTotal_collections(int total_collections) {
            this.total_collections = total_collections;
        }

        public int getTotal_likes() {
            return total_likes;
        }

        public void setTotal_likes(int total_likes) {
            this.total_likes = total_likes;
        }

        public int getTotal_photos() {
            return total_photos;
        }

        public void setTotal_photos(int total_photos) {
            this.total_photos = total_photos;
        }

        public boolean isAccepted_tos() {
            return accepted_tos;
        }

        public void setAccepted_tos(boolean accepted_tos) {
            this.accepted_tos = accepted_tos;
        }

        public static class LinksBeanX {
            /**
             * self : https://api.unsplash.com/users/ramche
             * html : https://unsplash.com/@ramche
             * photos : https://api.unsplash.com/users/ramche/photos
             * likes : https://api.unsplash.com/users/ramche/likes
             * portfolio : https://api.unsplash.com/users/ramche/portfolio
             * following : https://api.unsplash.com/users/ramche/following
             * followers : https://api.unsplash.com/users/ramche/followers
             */

            private String self;
            private String html;
            private String photos;
            private String likes;
            private String portfolio;
            private String following;
            private String followers;

            public String getSelf() {
                return self;
            }

            public void setSelf(String self) {
                this.self = self;
            }

            public String getHtml() {
                return html;
            }

            public void setHtml(String html) {
                this.html = html;
            }

            public String getPhotos() {
                return photos;
            }

            public void setPhotos(String photos) {
                this.photos = photos;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getPortfolio() {
                return portfolio;
            }

            public void setPortfolio(String portfolio) {
                this.portfolio = portfolio;
            }

            public String getFollowing() {
                return following;
            }

            public void setFollowing(String following) {
                this.following = following;
            }

            public String getFollowers() {
                return followers;
            }

            public void setFollowers(String followers) {
                this.followers = followers;
            }
        }

        public static class ProfileImageBean {
            /**
             * small : https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32
             * medium : https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64
             * large : https://images.unsplash.com/profile-1548418196506-4b6079886fd9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128
             */

            private String small;
            private String medium;
            private String large;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }
        }
    }

    public static class ExifBean {
        /**
         * make : Canon
         * model : Canon EOS R
         * exposure_time : 1/60
         * aperture : 2.0
         * focal_length : 35.0
         * iso : 800
         */

        private String make;
        private String model;
        private String exposure_time;
        private String aperture;
        private String focal_length;
        private int iso;

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getExposure_time() {
            return exposure_time;
        }

        public void setExposure_time(String exposure_time) {
            this.exposure_time = exposure_time;
        }

        public String getAperture() {
            return aperture;
        }

        public void setAperture(String aperture) {
            this.aperture = aperture;
        }

        public String getFocal_length() {
            return focal_length;
        }

        public void setFocal_length(String focal_length) {
            this.focal_length = focal_length;
        }

        public int getIso() {
            return iso;
        }

        public void setIso(int iso) {
            this.iso = iso;
        }
    }

    public static class LocationBean {
        /**
         * title :
         * name : null
         * city : null
         * country : null
         * position : {"latitude":null,"longitude":null}
         */

        private String title;
        private Object name;
        private Object city;
        private Object country;
        private PositionBean position;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
        }

        public PositionBean getPosition() {
            return position;
        }

        public void setPosition(PositionBean position) {
            this.position = position;
        }

        public static class PositionBean {
            /**
             * latitude : null
             * longitude : null
             */

            private Object latitude;
            private Object longitude;

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }
        }
    }
}
