To change MessageDialog appearance user can pick one or more ways.

First way:
Override styles MessageDialogStyle, MessageDialogTitleStyle, MessageDialogTextStyle,
MessageDialogItemStyle and MessageDialogButtonStyle to change Message dialog appearance. 
Also override colorDialogDivider to change color of dividers. 
Override dg_dialog if you want to change background of the dialog.

Second way:
Set button and text appearance through methods for setting title, message or positive, neutral and
negative buttons. 
IMPORTANT! Appearance that was set through methods has priority over overridden
styles.

Third way:
Override layout df_message.xml or i_dialog_message.xml

Fourth way:
Message dialog is an open class, so user can inherit Message dialog and make more settings in the
inheritor.
