package openvr.assets.steamVR.input

enum class ActionType {
    Boolean, Single, Vector2, Vector3, Pose, Skeleton, Vibration;

    fun <Action : SteamVR_Action> create(): Action = when (this) {
        Boolean -> SteamVR_Action_Boolean()
        Single -> SteamVR_Action_Single()
        Vector2 -> SteamVR_Action_Vector2()
        Vector3 -> SteamVR_Action_Vector3()
        Pose -> SteamVR_Action_Pose()
        Skeleton -> SteamVR_Action_Skeleton()
        Vibration -> SteamVR_Action_Vibration()
    } as Action

    fun <SourceMap : SteamVR_Action_Source_Map> newSourceMap(): SourceMap = when (this) {
        Boolean -> SteamVR_Action_Boolean_Source_Map()
        Single -> SteamVR_Action_Single_Source_Map()
        Vector2 -> SteamVR_Action_Vector2_Source_Map()
        Vector3 -> SteamVR_Action_Vector3_Source_Map()
        Pose -> SteamVR_Action_Pose_Source_Map()
        Skeleton -> SteamVR_Action_Skeleton_Source_Map()
        Vibration -> SteamVR_Action_Vibration_Source_Map()
    } as SourceMap

    fun <SourceElement : SteamVR_Action_Source> newSourceElement(): SourceElement = when (this) {
        Boolean -> SteamVR_Action_Boolean_Source()
        Single -> SteamVR_Action_Single_Source()
        Vector2 -> SteamVR_Action_Vector2_Source()
        Vector3 -> SteamVR_Action_Vector3_Source()
        Pose -> SteamVR_Action_Pose_Source()
        Skeleton -> SteamVR_Action_Skeleton_Source()
        Vibration -> SteamVR_Action_Vibration_Source()
    } as SourceElement
}

fun <Action : SteamVR_Action> Action.newCreateType(): Action = when (this) {
    is SteamVR_Action_Boolean -> SteamVR_Action_Boolean()
    is SteamVR_Action_Single -> SteamVR_Action_Single()
    is SteamVR_Action_Vector2 -> SteamVR_Action_Vector2()
    is SteamVR_Action_Vector3 -> SteamVR_Action_Vector3()
    is SteamVR_Action_Pose -> SteamVR_Action_Pose()
    is SteamVR_Action_Skeleton -> SteamVR_Action_Skeleton()
    is SteamVR_Action_Vibration -> SteamVR_Action_Vibration()
    else -> error("Invalid")
} as Action

fun <Action, SourceMap> Action.newSourceMap(): SourceMap
        where Action : SteamVR_Action, SourceMap : SteamVR_Action_Source_Map = when (this) {
    is SteamVR_Action_Boolean -> SteamVR_Action_Boolean_Source_Map()
    is SteamVR_Action_Single -> SteamVR_Action_Single_Source_Map()
    is SteamVR_Action_Vector2 -> SteamVR_Action_Vector2_Source_Map()
    is SteamVR_Action_Vector3 -> SteamVR_Action_Vector3_Source_Map()
    is SteamVR_Action_Pose -> SteamVR_Action_Pose_Source_Map()
    is SteamVR_Action_Skeleton -> SteamVR_Action_Skeleton_Source_Map()
    is SteamVR_Action_Vibration -> SteamVR_Action_Vibration_Source_Map()
    else -> error("Invalid")
} as SourceMap

fun <Source> SteamVR_Action_Source_Map.newSourceElement(): Source
        where Source : SteamVR_Action_Source = when (this) {
    is SteamVR_Action_Boolean_Source_Map -> SteamVR_Action_Boolean_Source()
    is SteamVR_Action_Single_Source_Map -> SteamVR_Action_Single_Source()
    is SteamVR_Action_Vector2_Source_Map -> SteamVR_Action_Vector2_Source()
    is SteamVR_Action_Vector3_Source_Map -> SteamVR_Action_Vector3_Source()
    is SteamVR_Action_Pose_Source_Map<*> -> SteamVR_Action_Pose_Source()
    is SteamVR_Action_Skeleton_Source_Map -> SteamVR_Action_Skeleton_Source()
    is SteamVR_Action_Vibration_Source_Map -> SteamVR_Action_Vibration_Source()
    else -> error("Invalid")
} as Source