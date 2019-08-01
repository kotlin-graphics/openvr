package openvr.steamVR.interactionSystem.core.scripts

import openvr.steamVR.input.SkeletalMotionRangeChange
import openvr.steamVR.input.SteamVR_ActionSet
import openvr.steamVR.input.SteamVR_Skeleton_Poser

//======= Copyright (c) Valve Corporation, All rights reserved. ===============
//
// Purpose: This object will get hover events and can be attached to the hands
//
//=============================================================================

//-------------------------------------------------------------------------
class Interactable /*: MonoBehaviour*/ {
    /** Activates an action set on attach and deactivates on detach */
    var activateActionSetOnAttach: SteamVR_ActionSet? = null

    /** Hide the whole hand on attachment and show on detach */
    var hideHandOnAttach = true

    /** Hide the skeleton part of the hand on attachment and show on detach */
    var hideSkeletonOnAttach = false

    /** Hide the controller part of the hand on attachment and show on detach */
    var hideControllerOnAttach = false

    /** The integer in the animator to trigger on pickup. 0 for none */
    var handAnimationOnPickup = 0

    /** The range of motion to set on the skeleton. None for no change. */
    var setRangeOfMotionOnPickup = SkeletalMotionRangeChange.None

//    public delegate void OnAttachedToHandDelegate(Hand hand)
//    public delegate void OnDetachedFromHandDelegate(Hand hand)
//
//    public event OnAttachedToHandDelegate onAttachedToHand
//    public event OnDetachedFromHandDelegate onDetachedFromHand


    /** Specify whether you want to snap to the hand's object attachment point, or just the raw hand */
    var useHandObjectAttachmentPoint = true

    var attachEaseIn = false

//    public AnimationCurve snapAttachEaseInCurve = AnimationCurve.EaseInOut(0.0f, 0.0f, 1.0f, 1.0f)
    var snapAttachEaseInTime = 0.15f

    var snapAttachEaseInCompleted = false


    /** The skeleton pose to apply when grabbing. Can only set this or handFollowTransform. */
    var skeletonPoser:  SteamVR_Skeleton_Poser? = null

    /** Should the rendered hand lock on to and follow the object */
    var handFollowTransform= true


    /** Set whether or not you want this interactible to highlight when hovering over it */
    var highlightOnHover = true
//    protected MeshRenderer[] highlightRenderers
//    protected MeshRenderer[] existingRenderers
//    protected GameObject highlightHolder
//    protected SkinnedMeshRenderer[] highlightSkinnedRenderers
//    protected SkinnedMeshRenderer[] existingSkinnedRenderers
//    protected static Material highlightMat
//    [Tooltip("An array of child gameObjects to not render a highlight for. Things like transparent parts, vfx, etc.")]
//    public GameObject[] hideHighlight
//
//
//    [System.NonSerialized]
//    public Hand attachedToHand
//
//    [System.NonSerialized]
//    public Hand hoveringHand

    var isDestroying = false
        protected set
    var isHovering = false
        protected set
    var wasHovering = false
        protected set


    private fun awake()    {
//        skeletonPoser = getComponent<SteamVR_Skeleton_Poser>()
    }

    protected fun start()    {

//        highlightMat = (Material) Resources . Load ("SteamVR_HoverHighlight", typeof(Material))
//
//        if (highlightMat == null)
//            Debug.LogError("<b>[SteamVR Interaction]</b> Hover Highlight Material is missing. Please create a material named 'SteamVR_HoverHighlight' and place it in a Resources folder")

        if (skeletonPoser != null) {
            if (useHandObjectAttachmentPoint) {
                //Debug.LogWarning("<b>[SteamVR Interaction]</b> SkeletonPose and useHandObjectAttachmentPoint both set at the same time. Ignoring useHandObjectAttachmentPoint.");
                useHandObjectAttachmentPoint = false
            }
        }
    }

//    protected virtual bool ShouldIgnoreHighlight(Component component)
//    {
//        return ShouldIgnore(component.gameObject)
//    }
//
//    protected virtual bool ShouldIgnore(GameObject check)
//    {
//        for (int ignoreIndex = 0; ignoreIndex < hideHighlight.Length; ignoreIndex++)
//        {
//            if (check == hideHighlight[ignoreIndex])
//                return true
//        }
//
//        return false
//    }
//
//    protected virtual void CreateHighlightRenderers()
//    {
//        existingSkinnedRenderers = this.GetComponentsInChildren<SkinnedMeshRenderer>(true)
//        highlightHolder = new GameObject ("Highlighter")
//        highlightSkinnedRenderers = new SkinnedMeshRenderer [existingSkinnedRenderers.Length]
//
//        for (int skinnedIndex = 0; skinnedIndex < existingSkinnedRenderers.Length; skinnedIndex++)
//        {
//            SkinnedMeshRenderer existingSkinned = existingSkinnedRenderers [skinnedIndex]
//
//            if (ShouldIgnoreHighlight(existingSkinned))
//                continue
//
//            GameObject newSkinnedHolder = new GameObject("SkinnedHolder")
//            newSkinnedHolder.transform.parent = highlightHolder.transform
//            SkinnedMeshRenderer newSkinned = newSkinnedHolder . AddComponent < SkinnedMeshRenderer >()
//            Material[] materials = new Material[existingSkinned.sharedMaterials.Length]
//            for (int materialIndex = 0; materialIndex < materials.Length; materialIndex++)
//            {
//                materials[materialIndex] = highlightMat
//            }
//
//            newSkinned.sharedMaterials = materials
//            newSkinned.sharedMesh = existingSkinned.sharedMesh
//            newSkinned.rootBone = existingSkinned.rootBone
//            newSkinned.updateWhenOffscreen = existingSkinned.updateWhenOffscreen
//            newSkinned.bones = existingSkinned.bones
//
//            highlightSkinnedRenderers[skinnedIndex] = newSkinned
//        }
//
//        MeshFilter[] existingFilters = this.GetComponentsInChildren<MeshFilter>(true)
//        existingRenderers = new MeshRenderer [existingFilters.Length]
//        highlightRenderers = new MeshRenderer [existingFilters.Length]
//
//        for (int filterIndex = 0; filterIndex < existingFilters.Length; filterIndex++)
//        {
//            MeshFilter existingFilter = existingFilters [filterIndex]
//            MeshRenderer existingRenderer = existingFilter . GetComponent < MeshRenderer >()
//
//            if (existingFilter == null || existingRenderer == null || ShouldIgnoreHighlight(existingFilter))
//                continue
//
//            GameObject newFilterHolder = new GameObject("FilterHolder")
//            newFilterHolder.transform.parent = highlightHolder.transform
//            MeshFilter newFilter = newFilterHolder . AddComponent < MeshFilter >()
//            newFilter.sharedMesh = existingFilter.sharedMesh
//            MeshRenderer newRenderer = newFilterHolder . AddComponent < MeshRenderer >()
//
//            Material[] materials = new Material[existingRenderer.sharedMaterials.Length]
//            for (int materialIndex = 0; materialIndex < materials.Length; materialIndex++)
//            {
//                materials[materialIndex] = highlightMat
//            }
//            newRenderer.sharedMaterials = materials
//
//            highlightRenderers[filterIndex] = newRenderer
//            existingRenderers[filterIndex] = existingRenderer
//        }
//    }
//
//    protected virtual void UpdateHighlightRenderers()
//    {
//        if (highlightHolder == null)
//            return
//
//        for (int skinnedIndex = 0; skinnedIndex < existingSkinnedRenderers.Length; skinnedIndex++)
//        {
//            SkinnedMeshRenderer existingSkinned = existingSkinnedRenderers [skinnedIndex]
//            SkinnedMeshRenderer highlightSkinned = highlightSkinnedRenderers [skinnedIndex]
//
//            if (existingSkinned != null && highlightSkinned != null && attachedToHand == false) {
//                highlightSkinned.transform.position = existingSkinned.transform.position
//                highlightSkinned.transform.rotation = existingSkinned.transform.rotation
//                highlightSkinned.transform.localScale = existingSkinned.transform.lossyScale
//                highlightSkinned.localBounds = existingSkinned.localBounds
//                highlightSkinned.enabled = isHovering && existingSkinned.enabled && existingSkinned.gameObject.activeInHierarchy
//
//                int blendShapeCount = existingSkinned . sharedMesh . blendShapeCount
//                for (int blendShapeIndex = 0; blendShapeIndex < blendShapeCount; blendShapeIndex++)
//                {
//                    highlightSkinned.SetBlendShapeWeight(blendShapeIndex, existingSkinned.GetBlendShapeWeight(blendShapeIndex))
//                }
//            } else if (highlightSkinned != null)
//                highlightSkinned.enabled = false
//
//        }
//
//        for (int rendererIndex = 0; rendererIndex < highlightRenderers.Length; rendererIndex++)
//        {
//            MeshRenderer existingRenderer = existingRenderers [rendererIndex]
//            MeshRenderer highlightRenderer = highlightRenderers [rendererIndex]
//
//            if (existingRenderer != null && highlightRenderer != null && attachedToHand == false) {
//                highlightRenderer.transform.position = existingRenderer.transform.position
//                highlightRenderer.transform.rotation = existingRenderer.transform.rotation
//                highlightRenderer.transform.localScale = existingRenderer.transform.lossyScale
//                highlightRenderer.enabled = isHovering && existingRenderer.enabled && existingRenderer.gameObject.activeInHierarchy
//            } else if (highlightRenderer != null)
//                highlightRenderer.enabled = false
//        }
//    }
//
//    /// <summary>
//    /// Called when a Hand starts hovering over this object
//    /// </summary>
//    protected virtual void OnHandHoverBegin(Hand hand)
//    {
//        wasHovering = isHovering
//        isHovering = true
//
//        hoveringHand = hand
//
//        if (highlightOnHover == true) {
//            CreateHighlightRenderers()
//            UpdateHighlightRenderers()
//        }
//    }
//
//
//    /// <summary>
//    /// Called when a Hand stops hovering over this object
//    /// </summary>
//    private void OnHandHoverEnd(Hand hand)
//    {
//        wasHovering = isHovering
//        isHovering = false
//
//        if (highlightOnHover && highlightHolder != null)
//            Destroy(highlightHolder)
//    }
//
//    protected virtual void Update()
//    {
//        if (highlightOnHover) {
//            UpdateHighlightRenderers()
//
//            if (isHovering == false && highlightHolder != null)
//                Destroy(highlightHolder)
//        }
//    }
//
//
//    protected float blendToPoseTime = 0.1f
//    protected float releasePoseBlendTime = 0.2f
//
//    protected virtual void OnAttachedToHand(Hand hand)
//    {
//        if (activateActionSetOnAttach != null)
//            activateActionSetOnAttach.Activate(hand.handType)
//
//        if (onAttachedToHand != null) {
//            onAttachedToHand.Invoke(hand)
//        }
//
//        if (skeletonPoser != null && hand.skeleton != null) {
//            hand.skeleton.BlendToPoser(skeletonPoser, blendToPoseTime)
//        }
//
//        attachedToHand = hand
//    }
//
//    protected virtual void OnDetachedFromHand(Hand hand)
//    {
//        if (activateActionSetOnAttach != null) {
//            if (hand.otherHand == null || hand.otherHand.currentAttachedObjectInfo.HasValue == false ||
//                    (hand.otherHand.currentAttachedObjectInfo.Value.interactable != null &&
//                            hand.otherHand.currentAttachedObjectInfo.Value.interactable.activateActionSetOnAttach != this.activateActionSetOnAttach)) {
//                activateActionSetOnAttach.Deactivate(hand.handType)
//            }
//        }
//
//        if (onDetachedFromHand != null) {
//            onDetachedFromHand.Invoke(hand)
//        }
//
//
//        if (skeletonPoser != null) {
//            if (hand.skeleton != null)
//                hand.skeleton.BlendToSkeleton(releasePoseBlendTime)
//        }
//
//        attachedToHand = null
//    }
//
//    protected virtual void OnDestroy()
//    {
//        isDestroying = true
//
//        if (attachedToHand != null) {
//            attachedToHand.DetachObject(this.gameObject, false)
//            attachedToHand.skeleton.BlendToSkeleton(0.1f)
//        }
//
//        if (highlightHolder != null)
//            Destroy(highlightHolder)
//
//    }
//
//
//    protected virtual void OnDisable()
//    {
//        isDestroying = true
//
//        if (attachedToHand != null) {
//            attachedToHand.ForceHoverUnlock()
//        }
//
//        if (highlightHolder != null)
//            Destroy(highlightHolder)
//    }
}