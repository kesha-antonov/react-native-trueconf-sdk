//
//  TCTheme.h
//  SDKLibrary
//
//  Copyright © 2018 TrueConf. All rights reserved.
//
#ifndef _TCTHEME_
#define _TCTHEME_ 1
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef enum : NSUInteger {
    TCThemeUID_LightBlue, // has string name
    TCThemeUID_LightRed,  // has string name
    TCThemeUID_BlackBlue, // has string name
    TCThemeUID_Turquoise, // has string name Бирюзовый
    TCThemeUID_Server,    // downloaded from server
    TCThemeUID_None
} TCThemeUID; // theme unique IDs - their order should not change in future

@interface TCTheme : NSObject

@property (nonatomic) TCThemeUID uid; // theme unique ID - it should not change in future
@property (nonatomic) long index; // theme index
@property (nonatomic) long indexi; // index of -theme-X suffix for images
@property (nonatomic) NSInteger indexVisible; // index of theme in themes list. NSNotFound means theme is not visible to user
@property (nonatomic) UIColor* _Nullable color; // general scheme color - blue, red, black
@property (nonatomic) UIColor* _Nullable tintColor; // tint color - blue, red, blue for black scheme

@property (nonatomic) UIColor* _Nullable btnTextColor; // buttons text color
@property (nonatomic) UIColor* _Nullable btnTextDisabledColor; // buttons text color in disabled state
@property (nonatomic) UIColor* _Nullable btnTextHighlightedColor; // buttons text color in pressed state
@property (nonatomic) UIColor* _Nullable disabledConfBtnTextColor; // Conference buttons text color in disabled state
@property (nonatomic) UIColor* _Nullable disabledControlColor; // color for disabled controls (gray for black scheme for example)
@property (nonatomic) UIColor* _Nullable panelBGColor; // panels color - white, light red, black
@property (nonatomic) UIColor* _Nullable panelBGColor2; // panels color - light blue, light red, black
@property (nonatomic) UIColor* _Nullable infoBGColor; // info panel color - light blue, light red, dark gray
@property (nonatomic) UIColor* _Nullable infoBorderColor; // info panel border color - white, gray
@property (nonatomic) UIColor* _Nullable ccBGColor; // conf controls panel color - white, gray
@property (nonatomic) UIColor* _Nullable ccBGColor2; // conf controls panel color - white, dark gray
@property (nonatomic) UIColor* _Nullable cellSelectedBackground; // table rows selected state color - dark gray, light gray
@property (nonatomic) UIColor* _Nullable btnSelectedRedColor; // color for selected list button (End, Lstatus)
@property (nonatomic) UIColor* _Nullable btnSelectedRedHighlightedColor; // color in pressed state
@property (nonatomic) UIColor* _Nullable listArrowColor; // color for arrow in list/table
@property (nonatomic) UIColor* _Nullable boxBGColor; // color for some lists/tables - white, dark gray AND background color for Searchfields
@property (nonatomic) UIColor* _Nullable blackTextColor; // black text in light schemes, white text in dark schemes
@property (nonatomic) UIColor* _Nullable grayTextColor; // gray text in light schemes, another gray text in dark schemes
@property (nonatomic) UIColor* _Nullable tableBGColor; // table background (contacts, chats) - white, black
@property (nonatomic) UIColor* _Nullable tableSeparatorColor; // table separator color - light gray, dark gray
@property (nonatomic) UIBarStyle searchBarStyle; // light or dark search bar style
@property (nonatomic) UIStatusBarStyle statusBarStyle; // light or dark status bar style
@property (nonatomic) UIUserInterfaceStyle interfaceStyle; // light or dark UI style
@property (nonatomic) UIColor* _Nullable tableGroupBGColor; // color for cells if groups is ON - white, dark gray
@property (nonatomic) UIColor* _Nullable tableGroupBGColorNew;
@property (nonatomic) UIColor* _Nullable editFieldBGColor; // color for Edit field background - white, dark gray
@property (nonatomic) UIColor* _Nullable editFieldHintColor; // color for Edit field's Hin - light gray, darker gray
@property (nonatomic) UIKeyboardAppearance keyboardAppearance; // light or dark
@property (nonatomic) UIColor* _Nullable conferenceBGColor; // background color for conference view - white, black
@property (nonatomic) UIColor* _Nullable conferenceBGColorFullscreen; // background color for conference view in fullscreen - black
@property (nonatomic) UIColor* _Nullable renderBGColor; // background color for vertical render
@property (nonatomic) BOOL windowBorder;
@property (nonatomic) UIColor* _Nullable windowBorderColor;
@property (nonatomic) UIColor* _Nullable tableCellOwnerBGColor; // conference owner's table cell BG color - light yellow
@property (nonatomic) UIBlurEffectStyle blurEffectStyle;
@property (nonatomic) UIColor* _Nullable groupBGColor; // color behind the groups - black
@property (nonatomic) UIColor* _Nullable groupBGiPadColor; // color behind the groups on iPad - gray
@property (nonatomic) UIColor* _Nullable inviteBtnForGroupiPadColor; // invite friends - button in the bottom of the contacts list. Usually it is of Button color. But in some cases (light theme on iPad) it could be white or black
@property (nonatomic) UIColor* _Nullable avatarPlaceholderColor; // view BG color - light gray, dark gray
@property (nonatomic) UIColor* _Nullable avatarPlaceholderTextBGColor; // text BG color - (darker) light gray, (lighter) dark gray
@property (nonatomic) UIColor* _Nullable avatarPlaceholderTextBGImgColor; // text BG color (with image) - gray
@property (nonatomic) UIColor* _Nullable avatarPlaceholderTextColor; // text color - white
@property (nonatomic) UIColor* _Nullable avatarPlaceholderTextImgColor; // text color (with image) - white
@property (nonatomic) BOOL signWithAppleIDButtonWhite; // is "Sign In With Apple" button white?
@property (nonatomic) NSString* _Nullable localizedName; // creative localizable name
@property (nonatomic) UIColor* _Nullable whiteToDarkGrayColor; // colorizing views depending on theme
@property (nonatomic) UIColor* _Nullable chatMessageGrayColorMy; // light gray for Reply and Forward small texts
@property (nonatomic) UIColor* _Nullable chatMessageGrayColorOther; // dark gray for Reply and Forward small texts
@property (nonatomic) UIColor* _Nullable chatMessageColorMy; // white color text message from me
@property (nonatomic) UIColor* _Nullable chatMessageColorOther; // black color text message from others
@property (nonatomic) UIColor* _Nullable chatURLColorMy; // color of the URL inside chat message from me
@property (nonatomic) UIColor* _Nullable chatURLColorOther; // color of the URL inside chat message from others
@property (nonatomic) UIColor* _Nullable chatsHintBgColor; // color of the table background when there are zero chats
@property (nonatomic) UIColor* _Nullable chatSubtitleYouColor; // You: xxxxx
@property (nonatomic) UIColor* _Nullable chatSubtitleHeColor; // Name: xxxxx
@property (nonatomic) UIColor* _Nullable chatSubtitleDraftColor; // Draft: xxxxx
@property (nonatomic) UIColor* _Nullable mainTabBarBgColor; // color of the main tab bar background
@property (nonatomic) BOOL ccTitles; // show Text Titles in conf controls OR do not
@property (nonatomic) int ccIndent; // indents for for HCollectionItem.img . Default = 8
@property (nonatomic) UIColor* _Nullable swipeReplyButtonColor;
@property (nonatomic) UIColor* _Nullable alwaysLightGrayColor;

// properties for downloaded themes

@property (nonatomic) BOOL defaultTheme; // use as default (if there are several themes from server)
@property (nonatomic) NSDate* _Nullable imagesDate;
@property (nonatomic) NSDate* _Nullable imagesDateSaved; // read from NSUserDefaults
@property (nonatomic) NSDictionary* _Nullable strings;
@property (nonatomic) NSURL* _Nullable imagesFile;
@property (nonatomic) NSDictionary<NSString*,NSString*>* _Nullable images; // image names and corresponding file names [from json]
@property (nonatomic) NSMutableArray<NSString*>* _Nullable imagesFiles;
@property (nonatomic) NSString* _Nullable icon;

//MARK: - color properties for new login flow
@property (nonatomic) UIColor* _Nullable nextBtnBGEnable;       // "next" button enable mode
@property (nonatomic) UIColor* _Nullable nextBtnBGDisable;      // "next" button disable mode
@property (nonatomic) UIColor* _Nullable editFieldBGColor2;     // textFields background color
@property (nonatomic) UIColor* _Nullable nextBtnTextDisable;    // "next" button text color disable
@property (nonatomic) UIColor* _Nullable nextBtnTextEnable;     // "next" button text color enable
@property (nonatomic) UIColor* _Nullable editFieldBGDisable;    // textFields background disable color

- (instancetype _Nonnull)copy;
- (UIImage* _Nullable) imageNamed:(NSString* _Nullable)img;
- (UIImage* _Nullable) imageNamed:(NSString* _Nullable)img inBundle:(nullable NSBundle *)bundle;
- (NSString* _Nullable) text:(NSString* _Nullable) txt;

- (void) fixUISegmentedControl:(UISegmentedControl* _Nonnull) sc titleFontAttribute:(NSDictionary* _Nullable) tfa tintColor:(UIColor* _Nullable) tc forTheme:(TCTheme* _Nullable) th;


- (instancetype _Nonnull)init;
- (instancetype _Nonnull)initWithID:(TCThemeUID) tid;

@end
#endif
