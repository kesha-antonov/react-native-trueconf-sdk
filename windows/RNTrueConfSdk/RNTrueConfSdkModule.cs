using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Trueconf.React.Sdk.RNTrueConfSdk
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNTrueConfSdkModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNTrueConfSdkModule"/>.
        /// </summary>
        internal RNTrueConfSdkModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNTrueConfSdk";
            }
        }
    }
}
