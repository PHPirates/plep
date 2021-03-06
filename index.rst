|BuildStatus| |DocumentationStatus|

====
Plep
====

Plepping is the new planning.

Plep works and looks like a real agenda, but with the advantages of having it on your computer.

Downloads
---------

Latest stable version:

.. image:: https://img.shields.io/github/release/deltadak/plep.svg?maxAge=2592000
    :target: https://github.com/deltadak/plep/releases/latest
    :alt: Release
Latest pre-release version:

.. image:: https://img.shields.io/github/release/deltadak/plep/all.svg?maxAge=2592000
    :target: https://github.com/deltadak/plep/releases
    :alt: Pre-release
|
We try to adhere to `Semantic Versioning`_. An update with a new first
number means it won’t work with your old database.

Features
--------

-  Drag and drop
-  Custom labels
-  Subtasks
-  Custom colours
-  Repeat for x weeks
-  Customize number of days and columns
-  Turn pages by a custom amount of days
-  Deletion and undoing deletion
-  Plep will remember all deleted tasks until you close it.

Note: The first time plep is ran, it will create a database file in the
folder where the jar is stored. Check for this file, otherwise your data
won’t be saved. You can backup this if you don’t want to lose your data.

Updating Plep on Linux
----------------------

Because of file permissions, an empty database with the correct permissions is created in ``/usr/lib/plep/lib/plep.db`` when the package is installed.
If you want to reuse an old database, backup that first and then replace it again (e.g. with ``cp -a --remove-destination plep_backup.db /usr/lib/plep/lib/plep.db``).

Screenshot of v2.0.0
--------------------

|screenshot|

Settings of v2.0.0
------------------

|screenshotSettings|

Instructions for building from source in IntelliJ
-------------------------------------------------

To install java on Fedora, install the ``java-openjdk-devel`` package (the regular ``openjdk`` only contains the jre, and not the jdk).

Make sure you have JavaFX installed, for example on Arch Linux and Fedora you can install the ``java-openjfx`` package. If `javafx` still cannot be resolve, delete and re-add the SDK.

-  To run or debug, use the Gradle task ``run``.
-  To build an executable jar, use the task ``build``, the file is then
   in ``build/libs/``.
-  To run tests from IntelliJ, install the Spek (and possibly also Spek Framework) plugin.

Building a Windows release
--------------------------

- Update the version number in ``build.gradle.kts``
-  Run the gradle task launch4j/createExe
-  Open InnoSetup on the setup file in ``releasing/Windows/innoSetup/PlepInstaller.iss``
-  Update the version number
-  Click Build \| Compile, the generated installer will be next to the setup file.

- If Plep doesn't start, some exception was thrown. Try the Gradle task 'shadowJar' and execute the jar file generated in build/libs with `java -jar plep-vx.x.x-all.jar`.

Building an Arch Linux release
------------------------------

- Update the version number in ``PKGBUILD`` and ``build.gradle.kts``
- Make sure you have everything pushed (you don't have to have the ``PKGBUILD`` pushed, but all the other build files)
- Go to ``releasing/arch-linux``
- Run ``makepkg -f`` or to only repackage ``makepkg -f --repackage``
- The generated package file will be ``plep-vx.x.x-1-any.pkg.tar.xz``
- Check that it contains the right files with ``pacman -Qlp plep-vx.x.x-1-any.pkg.tar.xz``
- Install with ``sudo pacman -U plep-vx.x.x-1-any.pkg.tar.xz``

Building a Fedora release
-------------------------

- Make sure you have the ``rpm-build`` and ``rpmdevtools`` packages installed.
- Compress the sources into a ``tar`` file, using (in the ``plep`` directory): ``tar czf ~/rpmbuild/SOURCES/plep-x.x.x.tar.gz file1 file2 file3 ... filen``. You should include (at least) the following files/directories:
  - ``./gradle``
  - ``./releasing/fedora`` (it contains the ``plep.desktop`` file)
  - ``./src``
  - ``build.gradle.kts``
  - ``gradlew``
  - ``gradlew.bat``
  - ``LICENSE``
  - ``settings.gradle.kts``
- Update the version number in ``plep.spec`` and ``build.gradle.kts``.
- Go to ``releasing/fedora``.
- Run ``rpmbuild -bb plep.spec`` to create the rpm file. This will create the rpm file in ``~rpmbuild/RPMS/noarch/``.
- Install the rpm with ``rpm -Uvh plep-x.x.x-y.fc29.noarch``, where ``x.x.x`` is the version number, and ``y`` the release number.


`Javadoc (obsolete)`_
---------------------

Badges with thanks to `shields.io <https://shields.io/>`_.

.. _Download latest version: https://github.com/deltadak/plep/releases
.. _Download stable version v1.2.2.: https://github.com/deltadak/plep/releases/download/v1.2.2/plep_v1.2.2.jar
.. _Download the experimental windows installer for v1.2.1.: https://github.com/deltadak/plep/releases/download/v1.2.1/setup_plep_v1.2.1.exe
.. _Semantic Versioning: http://semver.org/
.. _Javadoc (obsolete): http://htmlpreview.github.io/?https://github.com/deltadak/plep/blob/master/Javadoc/index.html


.. |BuildStatus| image:: https://travis-ci.org/deltadak/plep.svg?branch=master
    :target: https://travis-ci.org/deltadak/plep
    :alt: Build Status
.. |DocumentationStatus| image:: https://readthedocs.org/projects/plep/badge/?version=latest
    :target: http://plep.readthedocs.io/en/latest/?badge=latest
    :alt: Documentation Status
.. |OpenHub| image:: https://www.openhub.net/p/plep/widgets/project_thin_badge.gif
    :target: https://www.openhub.net/p/plep
    :alt: OpenHub
.. |screenshot| image:: screenshots/v2.0.0-beta.5.PNG
.. |screenshotSettings| image:: screenshots/v2.0.0-beta.5.settings.PNG