//package top.wifistar.bean;// automatically generated, do not modify
//
//import java.nio.*;
//import java.lang.*;
//import java.util.*;
//import com.google.flatbuffers.*;
//
//@SuppressWarnings("unused")
//public final class FlatbUserProfile extends Table {
//  public static FlatbUserProfile getRootAsFlatbUserProfile(ByteBuffer _bb) { return getRootAsFlatbUserProfile(_bb, new FlatbUserProfile()); }
//  public static FlatbUserProfile getRootAsFlatbUserProfile(ByteBuffer _bb, FlatbUserProfile obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
//  public FlatbUserProfile __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }
//
//  public String objectId() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer objectIdAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
//  public String userId() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer userIdAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
//  public String userName() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer userNameAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
//  public String birthday() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer birthdayAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
//  public String sex() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer sexAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
//  public String language() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer languageAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
//  public String moblie() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer moblieAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }
//  public String email() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer emailAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
//  public String description() { int o = __offset(20); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer descriptionAsByteBuffer() { return __vector_as_bytebuffer(20, 1); }
//  public String country() { int o = __offset(22); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer countryAsByteBuffer() { return __vector_as_bytebuffer(22, 1); }
//  public String state() { int o = __offset(24); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer stateAsByteBuffer() { return __vector_as_bytebuffer(24, 1); }
//  public String city() { int o = __offset(26); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer cityAsByteBuffer() { return __vector_as_bytebuffer(26, 1); }
//  public String avatar() { int o = __offset(28); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer avatarAsByteBuffer() { return __vector_as_bytebuffer(28, 1); }
//  public String pictures() { int o = __offset(30); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer picturesAsByteBuffer() { return __vector_as_bytebuffer(30, 1); }
//  public boolean invisible() { int o = __offset(32); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
//  public String blocks() { int o = __offset(34); return o != 0 ? __string(o + bb_pos) : null; }
//  public ByteBuffer blocksAsByteBuffer() { return __vector_as_bytebuffer(34, 1); }
//
//  public static int createFlatbUserProfile(FlatBufferBuilder builder,
//      int objectIdOffset,
//      int userIdOffset,
//      int userNameOffset,
//      int birthdayOffset,
//      int sexOffset,
//      int languageOffset,
//      int moblieOffset,
//      int emailOffset,
//      int descriptionOffset,
//      int countryOffset,
//      int stateOffset,
//      int cityOffset,
//      int avatarOffset,
//      int picturesOffset,
//      boolean invisible,
//      int blocksOffset) {
//    builder.startObject(16);
//    FlatbUserProfile.addBlocks(builder, blocksOffset);
//    FlatbUserProfile.addPictures(builder, picturesOffset);
//    FlatbUserProfile.addAvatar(builder, avatarOffset);
//    FlatbUserProfile.addCity(builder, cityOffset);
//    FlatbUserProfile.addState(builder, stateOffset);
//    FlatbUserProfile.addCountry(builder, countryOffset);
//    FlatbUserProfile.addDescription(builder, descriptionOffset);
//    FlatbUserProfile.addEmail(builder, emailOffset);
//    FlatbUserProfile.addMoblie(builder, moblieOffset);
//    FlatbUserProfile.addLanguage(builder, languageOffset);
//    FlatbUserProfile.addSex(builder, sexOffset);
//    FlatbUserProfile.addBirthday(builder, birthdayOffset);
//    FlatbUserProfile.addUserName(builder, userNameOffset);
//    FlatbUserProfile.addUserId(builder, userIdOffset);
//    FlatbUserProfile.addObjectId(builder, objectIdOffset);
//    FlatbUserProfile.addInvisible(builder, invisible);
//    return FlatbUserProfile.endFlatbUserProfile(builder);
//  }
//
//  public static void startFlatbUserProfile(FlatBufferBuilder builder) { builder.startObject(16); }
//  public static void addObjectId(FlatBufferBuilder builder, int objectIdOffset) { builder.addOffset(0, objectIdOffset, 0); }
//  public static void addUserId(FlatBufferBuilder builder, int userIdOffset) { builder.addOffset(1, userIdOffset, 0); }
//  public static void addUserName(FlatBufferBuilder builder, int userNameOffset) { builder.addOffset(2, userNameOffset, 0); }
//  public static void addBirthday(FlatBufferBuilder builder, int birthdayOffset) { builder.addOffset(3, birthdayOffset, 0); }
//  public static void addSex(FlatBufferBuilder builder, int sexOffset) { builder.addOffset(4, sexOffset, 0); }
//  public static void addLanguage(FlatBufferBuilder builder, int languageOffset) { builder.addOffset(5, languageOffset, 0); }
//  public static void addMoblie(FlatBufferBuilder builder, int moblieOffset) { builder.addOffset(6, moblieOffset, 0); }
//  public static void addEmail(FlatBufferBuilder builder, int emailOffset) { builder.addOffset(7, emailOffset, 0); }
//  public static void addDescription(FlatBufferBuilder builder, int descriptionOffset) { builder.addOffset(8, descriptionOffset, 0); }
//  public static void addCountry(FlatBufferBuilder builder, int countryOffset) { builder.addOffset(9, countryOffset, 0); }
//  public static void addState(FlatBufferBuilder builder, int stateOffset) { builder.addOffset(10, stateOffset, 0); }
//  public static void addCity(FlatBufferBuilder builder, int cityOffset) { builder.addOffset(11, cityOffset, 0); }
//  public static void addAvatar(FlatBufferBuilder builder, int avatarOffset) { builder.addOffset(12, avatarOffset, 0); }
//  public static void addPictures(FlatBufferBuilder builder, int picturesOffset) { builder.addOffset(13, picturesOffset, 0); }
//  public static void addInvisible(FlatBufferBuilder builder, boolean invisible) { builder.addBoolean(14, invisible, false); }
//  public static void addBlocks(FlatBufferBuilder builder, int blocksOffset) { builder.addOffset(15, blocksOffset, 0); }
//  public static int endFlatbUserProfile(FlatBufferBuilder builder) {
//    int o = builder.endObject();
//    return o;
//  }
//  public static void finishFlatbUserProfileBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
//};
//
