
Pod::Spec.new do |s|
  s.name         = "react-native-trueconf-react-sdk"
  s.version      = "1.0.1"
  s.summary      = "react-native-trueconf-react-sdk"
  s.description  = <<-DESC
                  Wrapper for TrueConf React SDK
                   DESC
  s.homepage     = "https://github.com/kesha-antonov/react-native-trueconf-react-sdk.git"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/kesha-antonov/react-native-trueconf-react-sdk.git", :tag => "master" }
  s.source_files  = "**/*.{h,m,swift}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end
