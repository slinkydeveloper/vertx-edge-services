package io.slinkydeveloper.brewery.styles;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: styles.proto")
public final class StylesServiceGrpc {

  private StylesServiceGrpc() {}

  private static <T> io.grpc.stub.StreamObserver<T> toObserver(final io.vertx.core.Handler<io.vertx.core.AsyncResult<T>> handler) {
    return new io.grpc.stub.StreamObserver<T>() {
      private volatile boolean resolved = false;
      @Override
      public void onNext(T value) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture(value));
        }
      }

      @Override
      public void onError(Throwable t) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.failedFuture(t));
        }
      }

      @Override
      public void onCompleted() {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
    };
  }

  public static final String SERVICE_NAME = "styles.StylesService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetStyleMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Style> METHOD_GET_STYLE = getGetStyleMethod();

  private static volatile io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Style> getGetStyleMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Style> getGetStyleMethod() {
    io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId, io.slinkydeveloper.brewery.styles.Style> getGetStyleMethod;
    if ((getGetStyleMethod = StylesServiceGrpc.getGetStyleMethod) == null) {
      synchronized (StylesServiceGrpc.class) {
        if ((getGetStyleMethod = StylesServiceGrpc.getGetStyleMethod) == null) {
          StylesServiceGrpc.getGetStyleMethod = getGetStyleMethod = 
              io.grpc.MethodDescriptor.<io.slinkydeveloper.brewery.styles.StyleId, io.slinkydeveloper.brewery.styles.Style>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "styles.StylesService", "GetStyle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.StyleId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.Style.getDefaultInstance()))
                  .setSchemaDescriptor(new StylesServiceMethodDescriptorSupplier("GetStyle"))
                  .build();
          }
        }
     }
     return getGetStyleMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetStylesMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.Empty,
      io.slinkydeveloper.brewery.styles.Styles> METHOD_GET_STYLES = getGetStylesMethod();

  private static volatile io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.Empty,
      io.slinkydeveloper.brewery.styles.Styles> getGetStylesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.Empty,
      io.slinkydeveloper.brewery.styles.Styles> getGetStylesMethod() {
    io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.Empty, io.slinkydeveloper.brewery.styles.Styles> getGetStylesMethod;
    if ((getGetStylesMethod = StylesServiceGrpc.getGetStylesMethod) == null) {
      synchronized (StylesServiceGrpc.class) {
        if ((getGetStylesMethod = StylesServiceGrpc.getGetStylesMethod) == null) {
          StylesServiceGrpc.getGetStylesMethod = getGetStylesMethod = 
              io.grpc.MethodDescriptor.<io.slinkydeveloper.brewery.styles.Empty, io.slinkydeveloper.brewery.styles.Styles>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "styles.StylesService", "GetStyles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.Styles.getDefaultInstance()))
                  .setSchemaDescriptor(new StylesServiceMethodDescriptorSupplier("GetStyles"))
                  .build();
          }
        }
     }
     return getGetStylesMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAddStyleMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.NewStyle,
      io.slinkydeveloper.brewery.styles.StyleId> METHOD_ADD_STYLE = getAddStyleMethod();

  private static volatile io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.NewStyle,
      io.slinkydeveloper.brewery.styles.StyleId> getAddStyleMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.NewStyle,
      io.slinkydeveloper.brewery.styles.StyleId> getAddStyleMethod() {
    io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.NewStyle, io.slinkydeveloper.brewery.styles.StyleId> getAddStyleMethod;
    if ((getAddStyleMethod = StylesServiceGrpc.getAddStyleMethod) == null) {
      synchronized (StylesServiceGrpc.class) {
        if ((getAddStyleMethod = StylesServiceGrpc.getAddStyleMethod) == null) {
          StylesServiceGrpc.getAddStyleMethod = getAddStyleMethod = 
              io.grpc.MethodDescriptor.<io.slinkydeveloper.brewery.styles.NewStyle, io.slinkydeveloper.brewery.styles.StyleId>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "styles.StylesService", "AddStyle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.NewStyle.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.StyleId.getDefaultInstance()))
                  .setSchemaDescriptor(new StylesServiceMethodDescriptorSupplier("AddStyle"))
                  .build();
          }
        }
     }
     return getAddStyleMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRemoveStyleMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Empty> METHOD_REMOVE_STYLE = getRemoveStyleMethod();

  private static volatile io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Empty> getRemoveStyleMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId,
      io.slinkydeveloper.brewery.styles.Empty> getRemoveStyleMethod() {
    io.grpc.MethodDescriptor<io.slinkydeveloper.brewery.styles.StyleId, io.slinkydeveloper.brewery.styles.Empty> getRemoveStyleMethod;
    if ((getRemoveStyleMethod = StylesServiceGrpc.getRemoveStyleMethod) == null) {
      synchronized (StylesServiceGrpc.class) {
        if ((getRemoveStyleMethod = StylesServiceGrpc.getRemoveStyleMethod) == null) {
          StylesServiceGrpc.getRemoveStyleMethod = getRemoveStyleMethod = 
              io.grpc.MethodDescriptor.<io.slinkydeveloper.brewery.styles.StyleId, io.slinkydeveloper.brewery.styles.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "styles.StylesService", "RemoveStyle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.StyleId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.slinkydeveloper.brewery.styles.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new StylesServiceMethodDescriptorSupplier("RemoveStyle"))
                  .build();
          }
        }
     }
     return getRemoveStyleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StylesServiceStub newStub(io.grpc.Channel channel) {
    return new StylesServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StylesServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StylesServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StylesServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StylesServiceFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static StylesServiceVertxStub newVertxStub(io.grpc.Channel channel) {
    return new StylesServiceVertxStub(channel);
  }

  /**
   */
  public static abstract class StylesServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Style> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStyleMethod(), responseObserver);
    }

    /**
     */
    public void getStyles(io.slinkydeveloper.brewery.styles.Empty request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Styles> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStylesMethod(), responseObserver);
    }

    /**
     */
    public void addStyle(io.slinkydeveloper.brewery.styles.NewStyle request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.StyleId> responseObserver) {
      asyncUnimplementedUnaryCall(getAddStyleMethod(), responseObserver);
    }

    /**
     */
    public void removeStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveStyleMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetStyleMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.slinkydeveloper.brewery.styles.StyleId,
                io.slinkydeveloper.brewery.styles.Style>(
                  this, METHODID_GET_STYLE)))
          .addMethod(
            getGetStylesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.slinkydeveloper.brewery.styles.Empty,
                io.slinkydeveloper.brewery.styles.Styles>(
                  this, METHODID_GET_STYLES)))
          .addMethod(
            getAddStyleMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.slinkydeveloper.brewery.styles.NewStyle,
                io.slinkydeveloper.brewery.styles.StyleId>(
                  this, METHODID_ADD_STYLE)))
          .addMethod(
            getRemoveStyleMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.slinkydeveloper.brewery.styles.StyleId,
                io.slinkydeveloper.brewery.styles.Empty>(
                  this, METHODID_REMOVE_STYLE)))
          .build();
    }
  }

  /**
   */
  public static final class StylesServiceStub extends io.grpc.stub.AbstractStub<StylesServiceStub> {
    public StylesServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    public StylesServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StylesServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StylesServiceStub(channel, callOptions);
    }

    /**
     */
    public void getStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Style> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStyleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStyles(io.slinkydeveloper.brewery.styles.Empty request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Styles> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStylesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addStyle(io.slinkydeveloper.brewery.styles.NewStyle request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.StyleId> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddStyleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveStyleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StylesServiceBlockingStub extends io.grpc.stub.AbstractStub<StylesServiceBlockingStub> {
    public StylesServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    public StylesServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StylesServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StylesServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public io.slinkydeveloper.brewery.styles.Style getStyle(io.slinkydeveloper.brewery.styles.StyleId request) {
      return blockingUnaryCall(
          getChannel(), getGetStyleMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.slinkydeveloper.brewery.styles.Styles getStyles(io.slinkydeveloper.brewery.styles.Empty request) {
      return blockingUnaryCall(
          getChannel(), getGetStylesMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.slinkydeveloper.brewery.styles.StyleId addStyle(io.slinkydeveloper.brewery.styles.NewStyle request) {
      return blockingUnaryCall(
          getChannel(), getAddStyleMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.slinkydeveloper.brewery.styles.Empty removeStyle(io.slinkydeveloper.brewery.styles.StyleId request) {
      return blockingUnaryCall(
          getChannel(), getRemoveStyleMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StylesServiceFutureStub extends io.grpc.stub.AbstractStub<StylesServiceFutureStub> {
    public StylesServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    public StylesServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StylesServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StylesServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.slinkydeveloper.brewery.styles.Style> getStyle(
        io.slinkydeveloper.brewery.styles.StyleId request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStyleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.slinkydeveloper.brewery.styles.Styles> getStyles(
        io.slinkydeveloper.brewery.styles.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStylesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.slinkydeveloper.brewery.styles.StyleId> addStyle(
        io.slinkydeveloper.brewery.styles.NewStyle request) {
      return futureUnaryCall(
          getChannel().newCall(getAddStyleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.slinkydeveloper.brewery.styles.Empty> removeStyle(
        io.slinkydeveloper.brewery.styles.StyleId request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveStyleMethod(), getCallOptions()), request);
    }
  }

  /**
   */
  public static abstract class StylesServiceVertxImplBase implements io.grpc.BindableService {

    /**
     */
    public void getStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Style> response) {
      asyncUnimplementedUnaryCall(getGetStyleMethod(), StylesServiceGrpc.toObserver(response.completer()));
    }

    /**
     */
    public void getStyles(io.slinkydeveloper.brewery.styles.Empty request,
        io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Styles> response) {
      asyncUnimplementedUnaryCall(getGetStylesMethod(), StylesServiceGrpc.toObserver(response.completer()));
    }

    /**
     */
    public void addStyle(io.slinkydeveloper.brewery.styles.NewStyle request,
        io.vertx.core.Future<io.slinkydeveloper.brewery.styles.StyleId> response) {
      asyncUnimplementedUnaryCall(getAddStyleMethod(), StylesServiceGrpc.toObserver(response.completer()));
    }

    /**
     */
    public void removeStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Empty> response) {
      asyncUnimplementedUnaryCall(getRemoveStyleMethod(), StylesServiceGrpc.toObserver(response.completer()));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetStyleMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.slinkydeveloper.brewery.styles.StyleId,
                io.slinkydeveloper.brewery.styles.Style>(
                  this, METHODID_GET_STYLE)))
          .addMethod(
            getGetStylesMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.slinkydeveloper.brewery.styles.Empty,
                io.slinkydeveloper.brewery.styles.Styles>(
                  this, METHODID_GET_STYLES)))
          .addMethod(
            getAddStyleMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.slinkydeveloper.brewery.styles.NewStyle,
                io.slinkydeveloper.brewery.styles.StyleId>(
                  this, METHODID_ADD_STYLE)))
          .addMethod(
            getRemoveStyleMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.slinkydeveloper.brewery.styles.StyleId,
                io.slinkydeveloper.brewery.styles.Empty>(
                  this, METHODID_REMOVE_STYLE)))
          .build();
    }
  }

  /**
   */
  public static final class StylesServiceVertxStub extends io.grpc.stub.AbstractStub<StylesServiceVertxStub> {
    public StylesServiceVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    public StylesServiceVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StylesServiceVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StylesServiceVertxStub(channel, callOptions);
    }

    /**
     */
    public void getStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.slinkydeveloper.brewery.styles.Style>> response) {
      asyncUnaryCall(
          getChannel().newCall(getGetStyleMethod(), getCallOptions()), request, StylesServiceGrpc.toObserver(response));
    }

    /**
     */
    public void getStyles(io.slinkydeveloper.brewery.styles.Empty request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.slinkydeveloper.brewery.styles.Styles>> response) {
      asyncUnaryCall(
          getChannel().newCall(getGetStylesMethod(), getCallOptions()), request, StylesServiceGrpc.toObserver(response));
    }

    /**
     */
    public void addStyle(io.slinkydeveloper.brewery.styles.NewStyle request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.slinkydeveloper.brewery.styles.StyleId>> response) {
      asyncUnaryCall(
          getChannel().newCall(getAddStyleMethod(), getCallOptions()), request, StylesServiceGrpc.toObserver(response));
    }

    /**
     */
    public void removeStyle(io.slinkydeveloper.brewery.styles.StyleId request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.slinkydeveloper.brewery.styles.Empty>> response) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveStyleMethod(), getCallOptions()), request, StylesServiceGrpc.toObserver(response));
    }
  }

  private static final int METHODID_GET_STYLE = 0;
  private static final int METHODID_GET_STYLES = 1;
  private static final int METHODID_ADD_STYLE = 2;
  private static final int METHODID_REMOVE_STYLE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StylesServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StylesServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_STYLE:
          serviceImpl.getStyle((io.slinkydeveloper.brewery.styles.StyleId) request,
              (io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Style>) responseObserver);
          break;
        case METHODID_GET_STYLES:
          serviceImpl.getStyles((io.slinkydeveloper.brewery.styles.Empty) request,
              (io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Styles>) responseObserver);
          break;
        case METHODID_ADD_STYLE:
          serviceImpl.addStyle((io.slinkydeveloper.brewery.styles.NewStyle) request,
              (io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.StyleId>) responseObserver);
          break;
        case METHODID_REMOVE_STYLE:
          serviceImpl.removeStyle((io.slinkydeveloper.brewery.styles.StyleId) request,
              (io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class VertxMethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StylesServiceVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(StylesServiceVertxImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_STYLE:
          serviceImpl.getStyle((io.slinkydeveloper.brewery.styles.StyleId) request,
              (io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Style>) io.vertx.core.Future.<io.slinkydeveloper.brewery.styles.Style>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Style>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        case METHODID_GET_STYLES:
          serviceImpl.getStyles((io.slinkydeveloper.brewery.styles.Empty) request,
              (io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Styles>) io.vertx.core.Future.<io.slinkydeveloper.brewery.styles.Styles>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Styles>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        case METHODID_ADD_STYLE:
          serviceImpl.addStyle((io.slinkydeveloper.brewery.styles.NewStyle) request,
              (io.vertx.core.Future<io.slinkydeveloper.brewery.styles.StyleId>) io.vertx.core.Future.<io.slinkydeveloper.brewery.styles.StyleId>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.StyleId>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        case METHODID_REMOVE_STYLE:
          serviceImpl.removeStyle((io.slinkydeveloper.brewery.styles.StyleId) request,
              (io.vertx.core.Future<io.slinkydeveloper.brewery.styles.Empty>) io.vertx.core.Future.<io.slinkydeveloper.brewery.styles.Empty>future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.slinkydeveloper.brewery.styles.Empty>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StylesServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StylesServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.slinkydeveloper.brewery.styles.StylesGRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StylesService");
    }
  }

  private static final class StylesServiceFileDescriptorSupplier
      extends StylesServiceBaseDescriptorSupplier {
    StylesServiceFileDescriptorSupplier() {}
  }

  private static final class StylesServiceMethodDescriptorSupplier
      extends StylesServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StylesServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StylesServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StylesServiceFileDescriptorSupplier())
              .addMethod(getGetStyleMethod())
              .addMethod(getGetStylesMethod())
              .addMethod(getAddStyleMethod())
              .addMethod(getRemoveStyleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
