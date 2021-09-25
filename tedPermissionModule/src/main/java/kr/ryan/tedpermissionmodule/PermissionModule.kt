package kr.ryan.tedpermissionmodule

import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermissionResult
import com.gun0912.tedpermission.coroutine.TedPermission

/**
 * MyFoodCalorie
 * Class: PermissionModule
 * Created by pyg10.
 * Created On 2021-09-25.
 * Description:
 */

suspend fun requireTedPermission(grant: () -> Unit, denied: () -> Unit, vararg permissions: String) {
    val result = TedPermission.create()
        .setPermissions(*permissions)
        .checkGranted()

    if (result)
        grant()
    else
        denied()
}



fun checkTedPermission(vararg permissions: String)
    = TedPermissionResult(permissions.toMutableList()).isGranted
