# openvr jvm binding

## Features:

- array classes `[]` operator, included `RenderModel_Vertex_t`
- concise enumerators, e.g. `EVRComponentProperty.VRComponentProperty_IsStatic` is `EVRComponentProperty.IsStatic`


## ChangeLog:

 OpenVR SDK 1.0.6:

General:
* Updated source code and cmake configurations for openvr_api.dll (and dylib/so) for applications that need a static library.
* Added VREvent_PropertyChanged event, which is sent when any property changes.
* Added VREvent_PrimaryDashboardDeviceChanged event, which is sent when the user changes the dashboard laser pointer from one controller to another.

IVRCompositor:
* Added initial support for DirectX 12 and OSX IOSurfaces. Use at your own risk. Forward compatibility is not guaranteed.
* Added IVRCompositor::ReleaseMirrorTextureD3D11(). Call ReleaseMirrorTextureD3D11 instead of calling Release directly on the texture.

IVRApplications:
* Added GetCurrentSceneProcessId(), which returns the process ID of the latest process to call VR_Init with the Scene application type.

Server driver Interface:
* Greatly simplified IServerTrackedDeviceProvider::Init and its arguments. This function now takes only an IVRDriverContext. From there it can call GetGenericInterface to get the rest of the interface.
* Added global accessor functions for drivers that are similar to those used by applications. Put this line at the start of your IServerTrackedDeviceProvider::Init function (and the equivalent line in Cleanup) to enable them:
 VR_INIT_SERVER_DRIVER_CONTEXT( pContext );
* IServerTrackedDeviceProvider no longer has enumeration functions for drivers. If the provider contains an HMD it should call TrackedDeviceAdded with the details of that HMD before Init returns. Other devices can be added at any time by calls to TrackedDeviceAdded.
* IVRServerDriverHost::TrackedDeviceAdded now takes all the required values for a new tracked device, including the device class and device driver interface pointer.
* Replace the property functions on ITrackedDeviceServerDriver with the IVRProperties interface and the CPropertyHelpers helper functions. This should result in significantly less boilerplate code in drivers and allows drivers to invalidate properties immediately instead of waiting for the client-side cache to expire. Use vr::VRProperties() to get the new helper interface. See the sample driver for details.
* Added a new "enable" setting to all drivers that will prevent the driver DLL from being loaded. The enable flag has been removed from the sample driver.
* IServerDriverHost has been renamed to IVRServerDriverHost no longer contains a few functions that are now handled by property setters.
 * GetSettings() is now handled with vr::vrSettings()
 * PhysicalIpdSet() is now handled by setting the Prop_UserIpdMeters_Float property.
 * TrackedDevicePropertiesChanged() is now handled automatically when a property is set.
 * MCImageUpdated() was undocumented and not useful outside the Lighthouse driver. It has been removed.

CVRHiddenAreaHelpers:
* This new helper class provides access to the hidden area mesh via the property system. You can access it with vr::VRHiddenArea() in a server driver.

IDriverLog:
* This interface has been renamed to IVRDriverLog

IClientTrackedDeviceProvider:
* Client drivers have been removed from the system. Drivers are no longer loaded into client processes. The functionality that used to be held in client drivers has moved:
 * BIsHmdPresent is implemented by looking for USB VID and PID values as specified in the driver manifest file: https://github.com/ValveSoftware/openvr/wiki/DriverManifest
 * GetHiddenAreaMesh is implemented via properties and the CVRHiddenAreaHelpers class. (See above)
 * GetMCImage was undocumented and not useful outside of the Lighthouse driver. It has been removed.
 * Watchdog mode, which allows SteamVR to start automatically on hardware activity, has been moved to a new driver type IVRWatchdogProvider.

IVRWatchdogProvider:
* This provider is only loaded in app type SteamWatchdog. It monitors the hardware for changes and calls vr::VRWatchdogHost()->WatchdogWakeUp() if an event occurs that should start SteamVR. This is entirely optional. A driver that doesn't implement this provider will just not wake up SteamVR on hardware activity.
