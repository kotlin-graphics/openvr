package openvr.plugin2

import openvr.lib.vrRenderModels
import org.lwjgl.openvr.TrackedDevicePose

class Events {

    abstract class Action {

        abstract fun enable(enabled: Boolean)
        var enabled: Boolean = false
            set(value) {
                enable(true)
                field = value
            }
    }

    //    [System.Serializable]
//    public class ActionNoArgs : Action
//    {
//        public ActionNoArgs(Event _event, UnityAction action)
//        {
//            this._event = _event;
//            this.action = action;
//        }
//
//        public override void Enable(bool enabled)
//        {
//            if (enabled)
//                _event.Listen(action);
//            else
//                _event.Remove(action);
//        }
//
//        Event _event;
//        UnityAction action;
//    }
//
//    [System.Serializable]
//    public class Action<T> : Action
//    {
//        public Action(Event<T> _event, UnityAction<T> action)
//        {
//            this._event = _event;
//            this.action = action;
//        }
//
//        public override void Enable(bool enabled)
//        {
//            if (enabled)
//                _event.Listen(action);
//            else
//                _event.Remove(action);
//        }
//
//        Event<T> _event;
//        UnityAction<T> action;
//    }
//
//    [System.Serializable]
//    public class Action<T0, T1> : Action
//    {
//        public Action(Event<T0, T1> _event, UnityAction<T0, T1> action)
//        {
//            this._event = _event;
//            this.action = action;
//        }
//
//        public override void Enable(bool enabled)
//        {
//            if (enabled)
//                _event.Listen(action);
//            else
//                _event.Remove(action);
//        }
//
//        Event<T0, T1> _event;
//        UnityAction<T0, T1> action;
//    }
//
//    [System.Serializable]
//    public class Action<T0, T1, T2> : Action
//    {
//        public Action(Event<T0, T1, T2> _event, UnityAction<T0, T1, T2> action)
//        {
//            this._event = _event;
//            this.action = action;
//        }
//
//        public override void Enable(bool enabled)
//        {
//            if (enabled)
//                _event.Listen(action);
//            else
//                _event.Remove(action);
//        }
//
//        Event<T0, T1, T2> _event;
//        UnityAction<T0, T1, T2> action;
//    }
//
//    public class Event : UnityEvent
//    {
//        public void Listen(UnityAction action) { this.AddListener(action); }
//        public void Remove(UnityAction action) { this.RemoveListener(action); }
//        public void Send() { this.Invoke(); }
//    }
//
    class Event0<T>/* : UnityEvent<T>*/ {
//        public void Listen(UnityAction<T> action) { this.AddListener(action); }
//        public void Remove(UnityAction<T> action) { this.RemoveListener(action); }
//        public void Send(T arg0) { this.Invoke(arg0); }
    }
//
    class Event1<T0, T1>/* : UnityEvent<T0, T1>*/ {
//        public void Listen(UnityAction<T0, T1> action) { this.AddListener(action); }
//        public void Remove(UnityAction<T0, T1> action) { this.RemoveListener(action); }
//        fun send(arg0: T0 , arg1: T1 ) { this.Invoke(arg0, arg1); }
    }
//
//    public class Event<T0, T1, T2> : UnityEvent<T0, T1, T2>
//    {
//        public void Listen(UnityAction<T0, T1, T2> action) { this.AddListener(action); }
//        public void Remove(UnityAction<T0, T1, T2> action) { this.RemoveListener(action); }
//        public void Send(T0 arg0, T1 arg1, T2 arg2) { this.Invoke(arg0, arg1, arg2); }
//    }

    companion object {
        //    public static Event<bool> Calibrating = new Event<bool>();
//    public static Action CalibratingAction(UnityAction<bool> action) { return new Action<bool>(Calibrating, action); }
//
//    public static Event<int, bool> DeviceConnected = new Event<int, bool>();
//    public static Action DeviceConnectedAction(UnityAction<int, bool> action) { return new Action<int, bool>(DeviceConnected, action); }
//
//    public static Event<Color, float, bool> Fade = new Event<Color, float, bool>();
//    public static Action FadeAction(UnityAction<Color, float, bool> action) { return new Action<Color, float, bool>(Fade, action); }
//
//    public static Event FadeReady = new Event();
//    public static Action FadeReadyAction(UnityAction action) { return new ActionNoArgs(FadeReady, action); }
//
//    public static Event<bool> HideRenderModels = new Event<bool>();
//    public static Action HideRenderModelsAction(UnityAction<bool> action) { return new Action<bool>(HideRenderModels, action); }
//
//    public static Event<bool> Initializing = new Event<bool>();
//    public static Action InitializingAction(UnityAction<bool> action) { return new Action<bool>(Initializing, action); }
//
//    public static Event<bool> InputFocus = new Event<bool>();
//    public static Action InputFocusAction(UnityAction<bool> action) { return new Action<bool>(InputFocus, action); }
//
//    public static Event<bool> Loading = new Event<bool>();
//    public static Action LoadingAction(UnityAction<bool> action) { return new Action<bool>(Loading, action); }
//
//    public static Event<float> LoadingFadeIn = new Event<float>();
//    public static Action LoadingFadeInAction(UnityAction<float> action) { return new Action<float>(LoadingFadeIn, action); }
//
//    public static Event<float> LoadingFadeOut = new Event<float>();
//    public static Action LoadingFadeOutAction(UnityAction<float> action) { return new Action<float>(LoadingFadeOut, action); }
//
        val newPoses = Event0<TrackedDevicePose.Buffer>()

//        fun newPosesAction(action: TrackedDevicePose.Buffer): Action = object : Action<TrackedDevicePose.Buffer>(newPoses, action)
//
//    public static Event NewPosesApplied = new Event();
//    public static Action NewPosesAppliedAction(UnityAction action) { return new ActionNoArgs(NewPosesApplied, action); }
//
//    public static Event<bool> Initialized = new Event<bool>();
//    public static Action InitializedAction(UnityAction<bool> action) { return new Action<bool>(Initialized, action); }
//
//    public static Event<bool> OutOfRange = new Event<bool>();
//    public static Action OutOfRangeAction(UnityAction<bool> action) { return new Action<bool>(OutOfRange, action); }
//
        val renderModelLoaded = Event1<vrRenderModels, Boolean>()
//    public static Action RenderModelLoadedAction(UnityAction<RenderModels, bool> action) { return new Action<RenderModels, bool>(RenderModelLoaded, action); }
//
//    static System.Collections.Generic.Dictionary<EVREventType, Event<VREvent_t>> systemEvents = new System.Collections.Generic.Dictionary<EVREventType, Event<VREvent_t>>();
//    public static Event<VREvent_t> System(EVREventType eventType)
//    {
//        Event<VREvent_t> e;
//        if (!systemEvents.TryGetValue(eventType, out e))
//        {
//            e = new Event<VREvent_t>();
//            systemEvents.Add(eventType, e);
//        }
//        return e;
//    }
//
//    public static Action SystemAction(EVREventType eventType, UnityAction<VREvent_t> action)
//    {
//        return new Action<VREvent_t>(System(eventType), action);
//    }
    }
}