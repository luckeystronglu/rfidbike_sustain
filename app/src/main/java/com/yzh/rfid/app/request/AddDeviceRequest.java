// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: AddDeviceRequest.proto

package com.yzh.rfid.app.request;

public final class AddDeviceRequest {
  private AddDeviceRequest() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface AddDeviceRequestMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:AddDeviceRequestMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string session = 1;</code>
     */
    String getSession();
    /**
     * <code>optional string session = 1;</code>
     */
    com.google.protobuf.ByteString
        getSessionBytes();

    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    boolean hasDeviceMessage();
    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    com.yzh.rfid.app.response.Device.DeviceMessage getDeviceMessage();
    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder getDeviceMessageOrBuilder();
  }
  /**
   * Protobuf type {@code AddDeviceRequestMessage}
   */
  public  static final class AddDeviceRequestMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:AddDeviceRequestMessage)
      AddDeviceRequestMessageOrBuilder {
    // Use AddDeviceRequestMessage.newBuilder() to construct.
    private AddDeviceRequestMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private AddDeviceRequestMessage() {
      session_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private AddDeviceRequestMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              String s = input.readStringRequireUtf8();

              session_ = s;
              break;
            }
            case 18: {
              com.yzh.rfid.app.response.Device.DeviceMessage.Builder subBuilder = null;
              if (deviceMessage_ != null) {
                subBuilder = deviceMessage_.toBuilder();
              }
              deviceMessage_ = input.readMessage(com.yzh.rfid.app.response.Device.DeviceMessage.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(deviceMessage_);
                deviceMessage_ = subBuilder.buildPartial();
              }

              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.yzh.rfid.app.request.AddDeviceRequest.internal_static_AddDeviceRequestMessage_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.yzh.rfid.app.request.AddDeviceRequest.internal_static_AddDeviceRequestMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.class, com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.Builder.class);
    }

    public static final int SESSION_FIELD_NUMBER = 1;
    private volatile Object session_;
    /**
     * <code>optional string session = 1;</code>
     */
    public String getSession() {
      Object ref = session_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        session_ = s;
        return s;
      }
    }
    /**
     * <code>optional string session = 1;</code>
     */
    public com.google.protobuf.ByteString
        getSessionBytes() {
      Object ref = session_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        session_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DEVICEMESSAGE_FIELD_NUMBER = 2;
    private com.yzh.rfid.app.response.Device.DeviceMessage deviceMessage_;
    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    public boolean hasDeviceMessage() {
      return deviceMessage_ != null;
    }
    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    public com.yzh.rfid.app.response.Device.DeviceMessage getDeviceMessage() {
      return deviceMessage_ == null ? com.yzh.rfid.app.response.Device.DeviceMessage.getDefaultInstance() : deviceMessage_;
    }
    /**
     * <code>optional .DeviceMessage deviceMessage = 2;</code>
     */
    public com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder getDeviceMessageOrBuilder() {
      return getDeviceMessage();
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getSessionBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, session_);
      }
      if (deviceMessage_ != null) {
        output.writeMessage(2, getDeviceMessage());
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getSessionBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, session_);
      }
      if (deviceMessage_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, getDeviceMessage());
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage)) {
        return super.equals(obj);
      }
      com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage other = (com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage) obj;

      boolean result = true;
      result = result && getSession()
          .equals(other.getSession());
      result = result && (hasDeviceMessage() == other.hasDeviceMessage());
      if (hasDeviceMessage()) {
        result = result && getDeviceMessage()
            .equals(other.getDeviceMessage());
      }
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + SESSION_FIELD_NUMBER;
      hash = (53 * hash) + getSession().hashCode();
      if (hasDeviceMessage()) {
        hash = (37 * hash) + DEVICEMESSAGE_FIELD_NUMBER;
        hash = (53 * hash) + getDeviceMessage().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code AddDeviceRequestMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:AddDeviceRequestMessage)
        com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.yzh.rfid.app.request.AddDeviceRequest.internal_static_AddDeviceRequestMessage_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.yzh.rfid.app.request.AddDeviceRequest.internal_static_AddDeviceRequestMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.class, com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.Builder.class);
      }

      // Construct using com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        session_ = "";

        if (deviceMessageBuilder_ == null) {
          deviceMessage_ = null;
        } else {
          deviceMessage_ = null;
          deviceMessageBuilder_ = null;
        }
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.yzh.rfid.app.request.AddDeviceRequest.internal_static_AddDeviceRequestMessage_descriptor;
      }

      public com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage getDefaultInstanceForType() {
        return com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.getDefaultInstance();
      }

      public com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage build() {
        com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage buildPartial() {
        com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage result = new com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage(this);
        result.session_ = session_;
        if (deviceMessageBuilder_ == null) {
          result.deviceMessage_ = deviceMessage_;
        } else {
          result.deviceMessage_ = deviceMessageBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage) {
          return mergeFrom((com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage other) {
        if (other == com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage.getDefaultInstance()) return this;
        if (!other.getSession().isEmpty()) {
          session_ = other.session_;
          onChanged();
        }
        if (other.hasDeviceMessage()) {
          mergeDeviceMessage(other.getDeviceMessage());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object session_ = "";
      /**
       * <code>optional string session = 1;</code>
       */
      public String getSession() {
        Object ref = session_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          session_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string session = 1;</code>
       */
      public com.google.protobuf.ByteString
          getSessionBytes() {
        Object ref = session_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          session_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string session = 1;</code>
       */
      public Builder setSession(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        session_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string session = 1;</code>
       */
      public Builder clearSession() {
        
        session_ = getDefaultInstance().getSession();
        onChanged();
        return this;
      }
      /**
       * <code>optional string session = 1;</code>
       */
      public Builder setSessionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        session_ = value;
        onChanged();
        return this;
      }

      private com.yzh.rfid.app.response.Device.DeviceMessage deviceMessage_ = null;
      private com.google.protobuf.SingleFieldBuilderV3<
          com.yzh.rfid.app.response.Device.DeviceMessage, com.yzh.rfid.app.response.Device.DeviceMessage.Builder, com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder> deviceMessageBuilder_;
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public boolean hasDeviceMessage() {
        return deviceMessageBuilder_ != null || deviceMessage_ != null;
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public com.yzh.rfid.app.response.Device.DeviceMessage getDeviceMessage() {
        if (deviceMessageBuilder_ == null) {
          return deviceMessage_ == null ? com.yzh.rfid.app.response.Device.DeviceMessage.getDefaultInstance() : deviceMessage_;
        } else {
          return deviceMessageBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public Builder setDeviceMessage(com.yzh.rfid.app.response.Device.DeviceMessage value) {
        if (deviceMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          deviceMessage_ = value;
          onChanged();
        } else {
          deviceMessageBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public Builder setDeviceMessage(
          com.yzh.rfid.app.response.Device.DeviceMessage.Builder builderForValue) {
        if (deviceMessageBuilder_ == null) {
          deviceMessage_ = builderForValue.build();
          onChanged();
        } else {
          deviceMessageBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public Builder mergeDeviceMessage(com.yzh.rfid.app.response.Device.DeviceMessage value) {
        if (deviceMessageBuilder_ == null) {
          if (deviceMessage_ != null) {
            deviceMessage_ =
              com.yzh.rfid.app.response.Device.DeviceMessage.newBuilder(deviceMessage_).mergeFrom(value).buildPartial();
          } else {
            deviceMessage_ = value;
          }
          onChanged();
        } else {
          deviceMessageBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public Builder clearDeviceMessage() {
        if (deviceMessageBuilder_ == null) {
          deviceMessage_ = null;
          onChanged();
        } else {
          deviceMessage_ = null;
          deviceMessageBuilder_ = null;
        }

        return this;
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public com.yzh.rfid.app.response.Device.DeviceMessage.Builder getDeviceMessageBuilder() {
        
        onChanged();
        return getDeviceMessageFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      public com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder getDeviceMessageOrBuilder() {
        if (deviceMessageBuilder_ != null) {
          return deviceMessageBuilder_.getMessageOrBuilder();
        } else {
          return deviceMessage_ == null ?
              com.yzh.rfid.app.response.Device.DeviceMessage.getDefaultInstance() : deviceMessage_;
        }
      }
      /**
       * <code>optional .DeviceMessage deviceMessage = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          com.yzh.rfid.app.response.Device.DeviceMessage, com.yzh.rfid.app.response.Device.DeviceMessage.Builder, com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder> 
          getDeviceMessageFieldBuilder() {
        if (deviceMessageBuilder_ == null) {
          deviceMessageBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              com.yzh.rfid.app.response.Device.DeviceMessage, com.yzh.rfid.app.response.Device.DeviceMessage.Builder, com.yzh.rfid.app.response.Device.DeviceMessageOrBuilder>(
                  getDeviceMessage(),
                  getParentForChildren(),
                  isClean());
          deviceMessage_ = null;
        }
        return deviceMessageBuilder_;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:AddDeviceRequestMessage)
    }

    // @@protoc_insertion_point(class_scope:AddDeviceRequestMessage)
    private static final com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage();
    }

    public static com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<AddDeviceRequestMessage>
        PARSER = new com.google.protobuf.AbstractParser<AddDeviceRequestMessage>() {
      public AddDeviceRequestMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new AddDeviceRequestMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<AddDeviceRequestMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<AddDeviceRequestMessage> getParserForType() {
      return PARSER;
    }

    public com.yzh.rfid.app.request.AddDeviceRequest.AddDeviceRequestMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AddDeviceRequestMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AddDeviceRequestMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\026AddDeviceRequest.proto\032\014Device.proto\"Q" +
      "\n\027AddDeviceRequestMessage\022\017\n\007session\030\001 \001" +
      "(\t\022%\n\rdeviceMessage\030\002 \001(\0132\016.DeviceMessag" +
      "eB0\n\034com.yzh.rfid.app.requestB\020AddDe" +
      "viceRequestb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.yzh.rfid.app.response.Device.getDescriptor(),
        }, assigner);
    internal_static_AddDeviceRequestMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_AddDeviceRequestMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AddDeviceRequestMessage_descriptor,
        new String[] { "Session", "DeviceMessage", });
    com.yzh.rfid.app.response.Device.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
