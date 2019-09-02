using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Trueconf.React.Sdk.RNTrueconfReactSdk
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNTrueconfReactSdkModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNTrueconfReactSdkModule"/>.
        /// </summary>
        internal RNTrueconfReactSdkModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNTrueconfReactSdk";
            }
        }
    }
}
