# DetectRemoveViewInDrawing
This is a util to detect view remove when it's parent is dispatching draw. As we all know, when you remove a view duration it's parent viewgroup dispatchdraw, you will get a java.lang.NullPointerException: Attempt to read from field 'int android.view.View.mViewFlags' on a null object reference
