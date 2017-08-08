# JVM OpenVR Binding

OpenVR SDK
---

This is the jvm port of OpenVR, which is an API and runtime that allows access to VR hardware from multiple 
vendors without requiring that applications have specific knowledge of the 
hardware they are targeting. This repository is an SDK that contains the API 
and samples. The runtime is under SteamVR in Tools on Steam. 

### Documentation

Documentation for the API is available on the [Github Wiki](https://github.com/ValveSoftware/openvr/wiki/API-Documentation)

More information on OpenVR and SteamVR can be found on http://steamvr.com

## Binding Features:

- original comments preserved and properly written
- every struct method is offered also much more user friendly. For example:
```kotlin
IVRSystem.GetProjectionMatrix(eEye: Int, fNearZ: Float, fFarZ: Float): HmdMatrix44_t.ByValue
``` 
because of the jna binding requirements, shall be called as:
```kotlin
IVRSystem.GetProjectionMatrix.invoke(eEye: Int, fNearZ: Float, fFarZ: Float): HmdMatrix44_t.ByValue
```
but there is also the possibility to simply call:
```kotlin
IVRSystem.getProjectionMatrix(eEye: EVREye, fNearZ: Float, fFarZ: Float): Mat4
```
that expects an `EVREye` enumerator type for `eEye` and returns directly a [glm](https://github.com/kotlin-graphics/glm) `Mat4`.
Or for example:
```kotlin
GetStringTrackedDeviceProperty.invoke(..): Int
```
where you are supposed to call it one first time to get the right size for the buffer to accomodate the char array for the string and then a second time to actually retrieve the string.
You have the possibility to call directly 
```kotlin
getStringTrackedDeviceProperty(..): String
```
that returns directly the resulting string, bringing down a lot of boilerplate code

- array classes `[]` operator, included `RenderModel_Vertex_t`
- concise enumerators, e.g. `EVRComponentProperty.VRComponentProperty_IsStatic` is `EVRComponentProperty.IsStatic`


### Sample:

You can find the OpenGL sample (using jogl) [here](https://github.com/java-opengl-labs/jogl-hello-vr)

Don't hesitate to contribute to the project by submitting [issues](https://github.com/kotlin-graphics/openvr/issues) or [pull requests](https://github.com/kotlin-graphics/openvr/pulls) for bugs and features. Any feedback is welcome at [elect86@gmail.com](mailto://elect86@gmail.com).


