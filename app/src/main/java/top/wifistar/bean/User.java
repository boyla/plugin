package top.wifistar.bean;// automatically generated, do not modify

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class User extends Table {
  public static User getRootAsUser(ByteBuffer _bb) { return getRootAsUser(_bb, new User()); }
  public static User getRootAsUser(ByteBuffer _bb, User obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public User __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public String account() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer accountAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String password() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer passwordAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public String id() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer idAsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public String name() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer nameAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public String gender() { int o = __offset(12); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer genderAsByteBuffer() { return __vector_as_bytebuffer(12, 1); }
  public String birthday() { int o = __offset(14); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer birthdayAsByteBuffer() { return __vector_as_bytebuffer(14, 1); }
  public String language() { int o = __offset(16); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer languageAsByteBuffer() { return __vector_as_bytebuffer(16, 1); }
  public String moblie() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer moblieAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
  public String email() { int o = __offset(20); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer emailAsByteBuffer() { return __vector_as_bytebuffer(20, 1); }
  public String description() { int o = __offset(22); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer descriptionAsByteBuffer() { return __vector_as_bytebuffer(22, 1); }
  public String country() { int o = __offset(24); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer countryAsByteBuffer() { return __vector_as_bytebuffer(24, 1); }
  public String state() { int o = __offset(26); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer stateAsByteBuffer() { return __vector_as_bytebuffer(26, 1); }
  public String city() { int o = __offset(28); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer cityAsByteBuffer() { return __vector_as_bytebuffer(28, 1); }
  public String avatarUrl() { int o = __offset(30); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer avatarUrlAsByteBuffer() { return __vector_as_bytebuffer(30, 1); }
  public String gravatarId() { int o = __offset(32); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer gravatarIdAsByteBuffer() { return __vector_as_bytebuffer(32, 1); }
  public boolean invisible() { int o = __offset(34); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public User contacts(int j) { return contacts(new User(), j); }
  public User contacts(User obj, int j) { int o = __offset(36); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int contactsLength() { int o = __offset(36); return o != 0 ? __vector_len(o) : 0; }
  public User blocks(int j) { return blocks(new User(), j); }
  public User blocks(User obj, int j) { int o = __offset(38); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int blocksLength() { int o = __offset(38); return o != 0 ? __vector_len(o) : 0; }

  public static int createUser(FlatBufferBuilder builder,
      int accountOffset,
      int passwordOffset,
      int idOffset,
      int nameOffset,
      int genderOffset,
      int birthdayOffset,
      int languageOffset,
      int moblieOffset,
      int emailOffset,
      int descriptionOffset,
      int countryOffset,
      int stateOffset,
      int cityOffset,
      int avatar_urlOffset,
      int gravatar_idOffset,
      boolean invisible,
      int contactsOffset,
      int blocksOffset) {
    builder.startObject(18);
    User.addBlocks(builder, blocksOffset);
    User.addContacts(builder, contactsOffset);
    User.addGravatarId(builder, gravatar_idOffset);
    User.addAvatarUrl(builder, avatar_urlOffset);
    User.addCity(builder, cityOffset);
    User.addState(builder, stateOffset);
    User.addCountry(builder, countryOffset);
    User.addDescription(builder, descriptionOffset);
    User.addEmail(builder, emailOffset);
    User.addMoblie(builder, moblieOffset);
    User.addLanguage(builder, languageOffset);
    User.addBirthday(builder, birthdayOffset);
    User.addGender(builder, genderOffset);
    User.addName(builder, nameOffset);
    User.addId(builder, idOffset);
    User.addPassword(builder, passwordOffset);
    User.addAccount(builder, accountOffset);
    User.addInvisible(builder, invisible);
    return User.endUser(builder);
  }

  public static void startUser(FlatBufferBuilder builder) { builder.startObject(18); }
  public static void addAccount(FlatBufferBuilder builder, int accountOffset) { builder.addOffset(0, accountOffset, 0); }
  public static void addPassword(FlatBufferBuilder builder, int passwordOffset) { builder.addOffset(1, passwordOffset, 0); }
  public static void addId(FlatBufferBuilder builder, int idOffset) { builder.addOffset(2, idOffset, 0); }
  public static void addName(FlatBufferBuilder builder, int nameOffset) { builder.addOffset(3, nameOffset, 0); }
  public static void addGender(FlatBufferBuilder builder, int genderOffset) { builder.addOffset(4, genderOffset, 0); }
  public static void addBirthday(FlatBufferBuilder builder, int birthdayOffset) { builder.addOffset(5, birthdayOffset, 0); }
  public static void addLanguage(FlatBufferBuilder builder, int languageOffset) { builder.addOffset(6, languageOffset, 0); }
  public static void addMoblie(FlatBufferBuilder builder, int moblieOffset) { builder.addOffset(7, moblieOffset, 0); }
  public static void addEmail(FlatBufferBuilder builder, int emailOffset) { builder.addOffset(8, emailOffset, 0); }
  public static void addDescription(FlatBufferBuilder builder, int descriptionOffset) { builder.addOffset(9, descriptionOffset, 0); }
  public static void addCountry(FlatBufferBuilder builder, int countryOffset) { builder.addOffset(10, countryOffset, 0); }
  public static void addState(FlatBufferBuilder builder, int stateOffset) { builder.addOffset(11, stateOffset, 0); }
  public static void addCity(FlatBufferBuilder builder, int cityOffset) { builder.addOffset(12, cityOffset, 0); }
  public static void addAvatarUrl(FlatBufferBuilder builder, int avatarUrlOffset) { builder.addOffset(13, avatarUrlOffset, 0); }
  public static void addGravatarId(FlatBufferBuilder builder, int gravatarIdOffset) { builder.addOffset(14, gravatarIdOffset, 0); }
  public static void addInvisible(FlatBufferBuilder builder, boolean invisible) { builder.addBoolean(15, invisible, false); }
  public static void addContacts(FlatBufferBuilder builder, int contactsOffset) { builder.addOffset(16, contactsOffset, 0); }
  public static int createContactsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startContactsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addBlocks(FlatBufferBuilder builder, int blocksOffset) { builder.addOffset(17, blocksOffset, 0); }
  public static int createBlocksVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBlocksVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endUser(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishUserBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
}

