# JVM OpenVR Wrapper (synchronized with 1.2.10)

[![Build Status](https://github.com/kotlin-graphics/openvr/workflows/build/badge.svg)](https://github.com/kotlin-graphics/openvr/actions?workflow=build)
[![license](https://img.shields.io/badge/License-BSD-3-Clause-orange.svg)](https://github.com/kotlin-graphics/openvr/blob/master/LICENSE) 
[![Release](https://jitpack.io/v/kotlin-graphics/openvr.svg)](https://jitpack.io/#kotlin-graphics/openvr) 
![Size](https://github-size-badge.herokuapp.com/kotlin-graphics/openvr.svg)
[![Github All Releases](https://img.shields.io/github/downloads/kotlin-graphics/openvr/total.svg)]()

OpenVR SDK
---

This is a kotlin wrapper of [OpenVR](https://github.com/ValveSoftware/openvr), which is an API and runtime that allows access to VR hardware from multiple 
vendors without requiring that applications have specific knowledge of the 
hardware they are targeting. This repository is an SDK that contains the API 
and samples. The runtime is under SteamVR in Tools on Steam. 

### Documentation

Documentation for the API is available on the [Github Wiki](https://github.com/ValveSoftware/openvr/wiki/API-Documentation)

More information on OpenVR and SteamVR can be found on http://steamvr.com

## Binding Features:

- original comments preserved and properly formatted
- direct fields, e.g: `var TrackedDevicePose.poseIsValid: Boolean`
- enumerators for type safety, e.g: `var Texture.type: TextureType`
- every struct method is offered also much more user friendly offering also full interoperability with glm, `getProjectionMatrix` returns, for example, directly a glm `Mat4`
```kotlin
vrSystem.getProjectionMatrix(eye: VREye, nearZ: Float, farZ: Float, res: Mat4 = Mat4()): Mat4
```
that expects an `EVREye` enumerator type for `eEye` and returns directly a [glm](https://github.com/kotlin-graphics/glm) `Mat4`.
instead of:
```kotlin: 
VRSystem.VRSystem_GetProjectionMatrix(eEye: Int, fNearZ: Float, fNear: Float, __result: HmdMatrix44): HmdMatrix44
```
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

- array classes `[]` operator, included `RenderModel_Vertex`
- concise enumerators, e.g. `EVRComponentProperty.VRComponentProperty_IsStatic` is `VRComponentProperty.IsStatic`
~- `SteamVRListener` for event listener. Instantiate a class extending it, call `.poll()` on it at the begin of each frame and override the corresponding methods you are looking for, such as `buttonPress(left: Boolean, button: EVRButtonId)`~ TO CHECK

### Sample:

You can find the OpenGL sample [here](src/test/kotlin/main/helloVr_OpenGL)

### Contributions:

Don't hesitate to contribute to the project by submitting [issues](https://github.com/kotlin-graphics/openvr/issues) or [pull requests](https://github.com/kotlin-graphics/openvr/pulls) for bugs and features. Any feedback is welcome at [elect86@gmail.com](mailto://elect86@gmail.com).


